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
	public boolean intersects(AABB area) {
		return true;
	}
	
	@Override
	public Optional<AABB> getAABB() {
		return Optional.of(AABB.INFINITE);
	}
}
