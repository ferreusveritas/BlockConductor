package com.ferreusveritas.block;

import com.ferreusveritas.support.json.JsonObj;

import java.util.HashMap;
import java.util.Map;

public class BlockCache {
	
	public static final Block NONE = new Block("", "");
	public static final Block AIR = new Block("minecraft:air", "");
	
	private Map<Block, Block> blocks = new HashMap<>();
	
	public BlockCache() {
		resolve(NONE);
		resolve(AIR);
	}
	
	public Block resolve(JsonObj src) {
		Block block = new Block(
			src.getString("name").orElseThrow(),
			src.getString("state").orElse("")
		);
		return resolve(block);
	}
	
	public Block resolve(Block block) {
		return blocks.computeIfAbsent(block, b -> b);
	}
	
}
