package com.ferreusveritas.block.provider;

import com.ferreusveritas.api.Request;
import com.ferreusveritas.block.Blocks;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RoutingBlockProvider extends BlockProvider {
	
	public static final String TYPE = "routing";
	
	private final Map<String, BlockProvider> providers;
	private final AABBI aabb;
	
	public RoutingBlockProvider(Scene scene, Map<String, BlockProvider> providers) {
		super(scene);
		this.providers = new HashMap<>(providers);
		this.aabb = unionProviders(this.providers.values());
	}
	
	public RoutingBlockProvider(Scene scene, JsonObj src) {
		super(scene, src);
		this.providers = src.getMap("providers").toImmutableMap(scene::createBlockProvider);
		this.aabb = unionProviders(this.providers.values());
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
	public Optional<AABBI> getAABB() {
		return Optional.of(aabb);
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set("providers", JsonObj.newMap(providers));
	}
	
}
