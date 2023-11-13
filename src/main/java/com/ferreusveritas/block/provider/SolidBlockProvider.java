package com.ferreusveritas.block.provider;

import com.ferreusveritas.api.*;
import com.ferreusveritas.block.Block;
import com.ferreusveritas.block.Blocks;
import com.ferreusveritas.math.AABBI;

import java.util.Optional;

public class SolidBlockProvider extends BlockProvider {

	private final Block block;

	public SolidBlockProvider(Block block) {
		this.block = block;
	}

	@Override
	public Optional<Blocks> getBlocks(Request request) {
		return Optional.of(new Blocks(request.area(), block));
	}
	
	@Override
	public boolean intersects(AABBI area) {
		return true;
	}
	
	@Override
	public Optional<AABBI> getAABB() {
		return Optional.of(AABBI.INFINITE);
	}
}
