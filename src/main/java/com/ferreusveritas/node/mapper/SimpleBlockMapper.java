package com.ferreusveritas.node.mapper;

import com.ferreusveritas.block.Block;
import com.ferreusveritas.block.BlockCache;
import com.ferreusveritas.node.NodeLoader;
import com.ferreusveritas.scene.LoaderSystem;
import com.ferreusveritas.support.json.JsonObj;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * A BlockMapper that uses a HashMap to map blocks.
 */
public class SimpleBlockMapper extends BlockMapper {
	
	public static final String TYPE = "simple";
	
	private final Map<Block, Block> map;
	private final Block defaultBlock;
	
	private SimpleBlockMapper(UUID uuid, Map<Block, Block> map, Block defaultBlock) {
		super(uuid);
		this.map = Map.copyOf(map);
		this.defaultBlock = defaultBlock == null ? BlockCache.NONE : defaultBlock;
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
	
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder {
		
		private UUID uuid = null;
		private final Map<Block, Block> map = new HashMap<>();
		private Block defaultBlock = BlockCache.NONE;
		
		public Builder uuid(UUID uuid) {
			this.uuid = uuid;
			return this;
		}
		
		public Builder add(Block in, Block out) {
			this.map.put(in, out);
			return this;
		}
		
		public Builder map(Map<Block, Block> map) {
			this.map.putAll(map);
			return this;
		}
		
		public Builder defaultBlock(Block defaultBlock) {
			this.defaultBlock = defaultBlock;
			return this;
		}
		
		public SimpleBlockMapper build() {
			return new SimpleBlockMapper(uuid, map, defaultBlock);
		}
		
	}
	
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends NodeLoader {
		
		private final List<BlockInOut> map;
		private final Block defaultBlock;
		
		public Loader(LoaderSystem loaderSystem, JsonObj src) {
			super(loaderSystem, src);
			this.map = src.getObj("map").orElseGet(JsonObj::newList).toImmutableList(j -> new BlockInOut(loaderSystem, j));
			this.defaultBlock = src.getObj("defaultBlock").map(loaderSystem::blockLoader).orElse(null);
		}
		
		@Override
		public BlockMapper load(LoaderSystem loaderSystem) {
			Map<Block, Block> blockMap = map.stream().collect(Collectors.toMap(BlockInOut::in, BlockInOut::out));
			return new SimpleBlockMapper(getUuid(), blockMap, defaultBlock);
		}
		
	}
	
}
