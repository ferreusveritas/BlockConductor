package com.ferreusveritas.block.provider;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferreusveritas.api.*;
import com.ferreusveritas.block.Block;
import com.ferreusveritas.block.Blocks;
import com.ferreusveritas.math.AABBI;

import java.util.Optional;

/**
 * A BlockProvider that provides a single block type for every position in the world.
 */
public class SolidBlockProvider extends BlockProvider {

	private final Block block;
	
	@JsonCreator
	public SolidBlockProvider(
		@JsonProperty("block") Block block
	) {
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
