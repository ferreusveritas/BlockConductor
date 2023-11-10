package com.ferreusveritas.block.provider;

import com.ferreusveritas.api.*;
import com.ferreusveritas.block.Block;
import com.ferreusveritas.block.BlockTypes;
import com.ferreusveritas.block.Blocks;
import com.ferreusveritas.math.AABB;
import com.ferreusveritas.math.VecI;

import java.util.List;
import java.util.Optional;

public class CombineBlockProvider extends BlockProvider {

	private final List<BlockProvider> providers;
	private final AABB aabb;
	
	public CombineBlockProvider(BlockProvider ... providers) {
		this.providers = List.of(providers);
		this.aabb = unionProviders(this.providers);
	}
	
	@Override
	public Optional<Blocks> getBlocks(Request request) {
		AABB area = request.area();
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
		AABB area = request.area();
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
	
	private void processBlock(Blocks blocksLayer, VecI pos, Blocks blocks) {
		Block block = blocksLayer.get(pos);
		if (block != BlockTypes.NONE) {
			blocks.set(pos, block);
		}
	}
	
	@Override
	public boolean intersects(AABB area) {
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
	public Optional<AABB> getAABB() {
		return Optional.of(aabb);
	}
	
}
