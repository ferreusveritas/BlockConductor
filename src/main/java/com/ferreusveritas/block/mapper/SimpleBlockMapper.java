package com.ferreusveritas.block.mapper;

import com.ferreusveritas.block.Block;
import com.ferreusveritas.block.BlockTypes;
import com.ferreusveritas.support.json.JsonObj;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A BlockMapper that uses a HashMap to map blocks.
 */
public class SimpleBlockMapper extends BlockMapper {
	
	public static final String TYPE = "simple";
	
	private final Map<Block, Block> map;
	private final Block defaultBlock;
	
	public SimpleBlockMapper(List<BlockInOut> map,Block defaultBlock) {
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
	
	public SimpleBlockMapper(JsonObj src) {
		super(src);
		this.map = src.getObj("map").orElseGet(JsonObj::newList).toImmutableList(BlockInOut::new).stream().collect(Collectors.toMap(BlockInOut::in, BlockInOut::out));
		this.defaultBlock = src.getObj("defaultBlock").map(Block::new).orElse(null);
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public Block map(Block block) {
		return map.getOrDefault(block, defaultBlock == null ? block : defaultBlock);
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set("map", map.entrySet().stream().map(e -> new BlockInOut(e.getKey(), e.getValue())).collect(Collectors.toList()))
			.set("defaultBlock", defaultBlock);
	}
	
}
