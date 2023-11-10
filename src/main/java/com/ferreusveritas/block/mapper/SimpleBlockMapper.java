package com.ferreusveritas.block.mapper;

import com.ferreusveritas.block.Block;

import java.util.Map;

/**
 * A BlockMapper that uses a HashMap to map blocks.
 */
public class SimpleBlockMapper implements BlockMapper {

	private final Map<Block, Block> map;
	private final Block defaultBlock;
	
	public SimpleBlockMapper(Map<Block, Block> mapper, Block defaultBlock) {
		this.map = Map.copyOf(mapper);
		this.defaultBlock = defaultBlock;
	}
	
	public SimpleBlockMapper(Map<Block, Block> mapper) {
		this(mapper, null);
	}
	
	@Override
	public Block map(Block block) {
		return map.getOrDefault(block, defaultBlock == null ? block : defaultBlock);
	}
	
}
