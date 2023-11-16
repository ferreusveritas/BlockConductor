package com.ferreusveritas.block;

public class BlockTypes {
	public static final Block NONE = new Block("", "");
	public static final Block AIR = new Block("minecraft:air", "");
	public static final Block STONE = new Block("minecraft:stone", "");
	public static final Block DIRT = new Block("minecraft:dirt", "");
	public static final Block SANDSTONE = new Block("minecraft:sandstone", "");
	public static final Block BONE = new Block("minecraft:bone_block", "");
	public static final Block BLACKSTONE = new Block("minecraft:blackstone", "");
	
	private BlockTypes() {
		throw new IllegalStateException("Utility class");
	}
	
}
