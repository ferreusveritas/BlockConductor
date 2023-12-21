package com.ferreusveritas.node.mapper;

import com.ferreusveritas.block.Block;
import com.ferreusveritas.node.Node;

import java.util.UUID;

/**
 * A BlockMapper is used to map one block to another.
 * This is useful for things like mapping a block to a different blockstate.
 */
public abstract class BlockMapper extends Node {
	
	protected BlockMapper(UUID uuid) {
		super(uuid);
	}
	
	public abstract Block map(Block block);
	
	public abstract String getType();
	
	@Override
	public Class<? extends Node> getNodeClass() {
		return BlockMapper.class;
	}
	
}
