package com.ferreusveritas.block;

import com.ferreusveritas.block.Block;

public class BlockTypes {
	public static final Block NONE = new Block("", "");
	public static final Block AIR = new Block("minecraft:air", "");
	public static final Block STONE = new Block("minecraft:stone", "");
	public static final Block DIRT = new Block("minecraft:dirt", "");
	public static final Block SANDSTONE = new Block("minecraft:sandstone", "");
	
	private BlockTypes() {
		throw new IllegalStateException("Utility class");
	}
	
}
