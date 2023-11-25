package com.ferreusveritas.block.provider;

import com.ferreusveritas.api.Request;
import com.ferreusveritas.block.Block;
import com.ferreusveritas.block.BlockCache;
import com.ferreusveritas.block.Blocks;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

import java.util.List;
import java.util.Optional;

/**
 * Combines multiple block providers into a single provider.
 * Each provider is processed in order. Blocks from each provider are overwritten by blocks from later providers.
 */
public class CombineBlockProvider extends BlockProvider {
	
	public static final String TYPE = "combine";
	
	private final List<BlockProvider> providers;
	private final AABBI aabb;
	
	public CombineBlockProvider(Scene scene, BlockProvider ... providers) {
		super(scene);
		this.providers = List.of(providers);
		this.aabb = unionProviders(this.providers);
	}
	
	public CombineBlockProvider(Scene scene, JsonObj src) {
		super(scene, src);
		this.providers = src.getList("providers").toImmutableList(scene::createBlockProvider);
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
		Blocks blocks = new Blocks(area);
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
	public Optional<AABBI> getAABB() {
		return Optional.of(aabb);
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set("providers", JsonObj.newList(providers));
	}
	
}
