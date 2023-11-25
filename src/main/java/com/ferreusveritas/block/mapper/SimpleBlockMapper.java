package com.ferreusveritas.block.mapper;

import com.ferreusveritas.block.Block;
import com.ferreusveritas.block.BlockCache;
import com.ferreusveritas.scene.Scene;
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
	
	public SimpleBlockMapper(Scene scene, List<BlockInOut> map, Block defaultBlock) {
		super(scene);
		this.map = map.stream().collect(Collectors.toMap(BlockInOut::in, BlockInOut::out));
		this.defaultBlock = defaultBlock == null ? BlockCache.NONE : defaultBlock;
	}
	
	public SimpleBlockMapper(Scene scene, Map<Block, Block> map, Block defaultBlock) {
		super(scene);
		this.map = Map.copyOf(map);
		this.defaultBlock = defaultBlock == null ? BlockCache.NONE : defaultBlock;
	}
	
	public SimpleBlockMapper(Scene scene, Map<Block, Block> mapper) {
		this(scene, mapper, null);
	}
	
	public SimpleBlockMapper(Scene scene, JsonObj src) {
		super(scene, src);
		this.map = src.getObj("map").orElseGet(JsonObj::newList).toImmutableList(j -> new BlockInOut(scene, j)).stream().collect(Collectors.toMap(BlockInOut::in, BlockInOut::out));
		this.defaultBlock = src.getObj("defaultBlock").map(scene::block).orElse(null);
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
			.set("map", map.entrySet().stream().map(e -> new BlockInOut(e.getKey(), e.getValue())).toList())
			.set("defaultBlock", defaultBlock);
	}
	
}
