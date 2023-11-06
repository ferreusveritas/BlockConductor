package com.ferreusveritas.blockproviders;

import com.ferreusveritas.api.*;

import java.util.Optional;

public class SolidBlockProvider extends BlockProvider {

	private final Block block;

	public SolidBlockProvider(Block block) {
		this.block = block;
	}

	@Override
	public Optional<Blocks> getBlocks(AABB area) {
		return Optional.of(new Blocks(area, block));
	}
	
	@Override
	public boolean intersects(AABB aabb) {
		return true;
	}
	
	@Override
	public AABB getAABB() {
		return AABB.INFINITE;
	}
	
}
