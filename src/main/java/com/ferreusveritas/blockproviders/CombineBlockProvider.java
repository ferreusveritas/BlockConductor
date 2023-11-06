package com.ferreusveritas.blockproviders;

import com.ferreusveritas.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CombineBlockProvider extends BlockProvider {

	private final List<BlockProvider> providers;
	private final AABB aabb;
	
	private CombineBlockProvider(Builder builder) {
		this.providers = List.copyOf(builder.providers);
		this.aabb = builder.aabb;
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
	public boolean intersects(AABB aabb) {
		if(!this.aabb.intersects(aabb)) {
			return false;
		}
		for (BlockProvider provider : providers) {
			if (provider.intersects(aabb)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public AABB getAABB() {
		return aabb;
	}
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder {
		
		private final List<BlockProvider> providers = new ArrayList<>();
		private AABB aabb;
		
		public Builder add(BlockProvider provider) {
			this.providers.add(provider);
			if(aabb == null) {
				aabb = provider.getAABB();
			} else {
				aabb = aabb.union(provider.getAABB());
			}
			return this;
		}
		
		public CombineBlockProvider build() {
			if(providers.isEmpty()) {
				throw new IllegalStateException("No providers added");
			}
			if(aabb == null) {
				throw new IllegalStateException("No AABB calculated");
			}
			return new CombineBlockProvider(this);
		}
	}
	
}
