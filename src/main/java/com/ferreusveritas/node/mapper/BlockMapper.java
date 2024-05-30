package com.ferreusveritas.node.mapper;

import com.ferreusveritas.block.Block;
import com.ferreusveritas.node.SceneObject;

import java.util.UUID;

/**
 * A BlockMapper is used to map one block to another.
 * This is useful for things like mapping a block to a different blockstate.
 */
public abstract class BlockMapper extends SceneObject {
	
	public static final String MAPPER = "mapper";
	public static final String BLOCK_MAPPER = "blockMapper";
	
	protected BlockMapper(UUID uuid) {
		super(uuid);
	}
	
	public abstract Block map(Block block);
	
}
