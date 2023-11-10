package com.ferreusveritas.block.mapper;

import com.ferreusveritas.block.Block;

/**
 * A BlockMapper is used to map one block to another.
 * This is useful for things like mapping a block to a different blockstate.
 */
public interface BlockMapper {
	Block map(Block block);
	
}
