package com.ferreusveritas.blockproviders;

import com.ferreusveritas.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CombineBlockProvider extends BlockProvider {

	private final List<BlockProvider> providers;
	private final AABB aabb;
	
	public CombineBlockProvider(BlockProvider ... providers) {
		this.providers = List.of(providers);
		this.aabb = calculateAABB(this.providers);
	}
	
	private static AABB calculateAABB(List<BlockProvider> providers) {
		AABB aabb = null;
		for (BlockProvider provider : providers) {
			Optional<AABB> providerAABB = provider.getAABB();
			if (providerAABB.isEmpty()) {
				continue;
			}
			if (aabb == null) {
				aabb = providerAABB.get();
			} else {
				aabb = aabb.union(providerAABB.get());
			}
		}
		return aabb;
	}
	
	@Override
	public Optional<Blocks> getBlocks(AABB area) {
		if (!this.aabb.intersects(area)) {
			return Optional.empty();
		}
		Blocks blocks = new Blocks(area);
		for (BlockProvider provider : providers) {
			processProvider(provider, area, blocks);
		}
		return Optional.of(blocks);
	}
	
	private void processProvider(BlockProvider provider, AABB area, Blocks blocks) {
		if(!provider.intersects(area)) {
			return;
		}
		Optional<Blocks> blocksOptional = provider.getBlocks(area);
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
