package com.ferreusveritas.node.provider;

import com.ferreusveritas.api.Request;
import com.ferreusveritas.block.Block;
import com.ferreusveritas.block.BlockCache;
import com.ferreusveritas.block.Blocks;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.node.NodeLoader;
import com.ferreusveritas.scene.LoaderSystem;
import com.ferreusveritas.support.json.JsonObj;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Combines multiple block providers into a single provider.
 * Each provider is processed in order. Blocks from each provider are overwritten by blocks from later providers.
 */
public class CombineBlockProvider extends BlockProvider {
	
	public static final String TYPE = "combine";
	public static final String PROVIDERS = "providers";
	
	private final List<BlockProvider> providers;
	private final AABBI aabb;
	
	private CombineBlockProvider(UUID uuid, List<BlockProvider> providers) {
		super(uuid);
		this.providers = providers;
		this.aabb = unionProviders(this.providers);
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public Optional<Blocks> getBlocks(Request request) {
		AABBI area = request.area();
		if (!this.aabb.intersects(area)) {
			return Optional.empty();
		}
		Blocks blocks = new Blocks(area.size());
		for (BlockProvider provider : providers) {
			processProvider(provider, request, blocks);
		}
		return Optional.of(blocks);
	}
	
	private void processProvider(BlockProvider provider, Request request, Blocks blocks) {
		AABBI area = request.area();
		if(!provider.intersects(area)) {
			return;
		}
		Optional<Blocks> blocksOptional = provider.getBlocks(request);
		if (blocksOptional.isEmpty()) {
			return;
		}
		Blocks blocksLayer = blocksOptional.get();
		area.forEach((abs, rel) -> processBlock(blocksLayer, rel, blocks));
	}
	
	private void processBlock(Blocks blocksLayer, Vec3I pos, Blocks blocks) {
		Block block = blocksLayer.get(pos);
		if (block != BlockCache.NONE) {
			blocks.set(pos, block);
		}
	}
	
	@Override
	public boolean intersects(AABBI area) {
		if(!this.aabb.intersects(area)) {
			return false;
		}
		for (BlockProvider provider : providers) {
			if (provider.intersects(area)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public AABBI getAABB() {
		return aabb;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(PROVIDERS, JsonObj.newList(providers));
	}
	
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder {
		
		private UUID uuid = null;
		private final List<BlockProvider> providers = new ArrayList<>();
		
		public Builder uuid(UUID uuid) {
			this.uuid = uuid;
			return this;
		}
		
		public Builder add(List<BlockProvider> providers) {
			this.providers.addAll(providers);
			return this;
		}
		
		public Builder add(BlockProvider ... providers) {
			this.providers.addAll(List.of(providers));
			return this;
		}
		
		public CombineBlockProvider build() {
			return new CombineBlockProvider(uuid, providers);
		}
		
	}
	
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends NodeLoader {
		
		private final List<NodeLoader> providers;
		
		public Loader(LoaderSystem loaderSystem, UUID uuid, JsonObj src) {
			super(uuid);
			this.providers = src.getList(PROVIDERS).toImmutableList(loaderSystem::createLoader);
		}
		
		@Override
		public BlockProvider load(LoaderSystem loaderSystem) {
			List<BlockProvider> b = providers.stream().map(l -> l.load(loaderSystem, BlockProvider.class).orElseThrow()).toList();
			return new CombineBlockProvider(getUuid(), b);
		}
		
	}
	
}
