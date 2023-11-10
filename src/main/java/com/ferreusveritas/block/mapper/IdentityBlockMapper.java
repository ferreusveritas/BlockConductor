package com.ferreusveritas.block.mapper;

import com.ferreusveritas.block.Block;

/**
 * A BlockMapper that does nothing.
 * Blocks in will match blocks out.
 */
public class IdentityBlockMapper implements BlockMapper {
	
	@Override
	public Block map(Block block) {
		return block;
	}
	
}
