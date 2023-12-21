package com.ferreusveritas.node.provider;

import com.ferreusveritas.api.Request;
import com.ferreusveritas.block.Blocks;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.node.NodeLoader;
import com.ferreusveritas.scene.LoaderSystem;
import com.ferreusveritas.support.json.JsonObj;

import java.util.*;

public class RoutingBlockProvider extends BlockProvider {
	
	public static final String TYPE = "routing";
	public static final String PROVIDERS = "providers";
	
	private final Map<String, BlockProvider> providers;
	private final AABBI aabb;
	
	private RoutingBlockProvider(UUID uuid, Map<String, BlockProvider> providers) {
		super(uuid);
		this.providers = new HashMap<>(providers);
		this.aabb = calculateBounds(this.providers.values());
	}
	
	private AABBI calculateBounds(Collection<BlockProvider> values) {
		return unionProviders(values);
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public Optional<Blocks> getBlocks(Request request) {
		BlockProvider provider = providers.get(request.context());
		if(provider != null) {
			return provider.getBlocks(request);
		}
		return Optional.empty();
	}
	
	@Override
	public AABBI getAABB() {
		return aabb;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(PROVIDERS, JsonObj.newMap(providers));
	}
	
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder {
		
		private UUID uuid = null;
		private final Map<String, BlockProvider> providers = new HashMap<>();
		
		public Builder uuid(UUID uuid) {
			this.uuid = uuid;
			return this;
		}
		
		public Builder add(String context, BlockProvider provider) {
			providers.put(context, provider);
			return this;
		}
		
		public Builder add(Map<String, BlockProvider> providers) {
			this.providers.putAll(providers);
			return this;
		}
		
		public BlockProvider build() {
			return new RoutingBlockProvider(uuid, Map.copyOf(providers));
		}
		
	}
	
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends NodeLoader {
		
		private final Map<String, NodeLoader> providers;
		
		public Loader(LoaderSystem loaderSystem, JsonObj src) {
			super(loaderSystem, src);
			this.providers = src.getMap(PROVIDERS).toImmutableMap(loaderSystem::createLoader);
		}
		
		@Override
		public BlockProvider load(LoaderSystem loaderSystem) {
			Builder builder = new Builder().uuid(getUuid());
			providers.forEach((key, value) -> builder.add(key, value.load(loaderSystem, BlockProvider.class).orElseThrow()));
			return builder.build();
		}
		
	}
	
}
