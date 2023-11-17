package com.ferreusveritas.block.mapper;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferreusveritas.block.Block;
import com.ferreusveritas.block.BlockTypes;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A BlockMapper that uses a HashMap to map blocks.
 */
public class SimpleBlockMapper implements BlockMapper {

	private final Map<Block, Block> map;
	private final Block defaultBlock;
	
	@JsonCreator
	public SimpleBlockMapper(
		@JsonProperty("map") List<BlockInOut> map,
		@JsonProperty("defaultBlock") Block defaultBlock
	) {
		this.map = map.stream().collect(Collectors.toMap(BlockInOut::in, BlockInOut::out));
		this.defaultBlock = defaultBlock == null ? BlockTypes.NONE : defaultBlock;
	}
	
	public SimpleBlockMapper(Map<Block, Block> map, Block defaultBlock) {
		this.map = Map.copyOf(map);
		this.defaultBlock = defaultBlock == null ? BlockTypes.NONE : defaultBlock;
	}
	
	public SimpleBlockMapper(Map<Block, Block> mapper) {
		this(mapper, null);
	}
	
	@Override
	public Block map(Block block) {
		return map.getOrDefault(block, defaultBlock == null ? block : defaultBlock);
	}
	
}
