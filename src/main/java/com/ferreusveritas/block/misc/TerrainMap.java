package com.ferreusveritas.block.misc;

import java.util.Arrays;

public class TerrainMap {
	
	private final int xSize;
	private final int zSize;
	private final int[] map; //The map of surface heights
	
	public TerrainMap(Builder builder) {
		this.xSize = builder.xSize;
		this.zSize = builder.zSize;
		this.map = Arrays.copyOf(builder.map, builder.map.length);
	}
	
	public int getXSize() {
		return xSize;
	}
	
	public int getZSize() {
		return zSize;
	}
	
	public int get(int x, int z) {
		return map[x + z * xSize];
	}
	
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder {
		
		private final int xSize;
		private final int zSize;
		private final int[] map; //The map of surface heights
		
		public Builder(int xSize, int zSize) {
			this.xSize = xSize;
			this.zSize = zSize;
			this.map = new int[xSize * zSize];
		}
		
		public Builder set(int x, int z, int value) {
			map[x + z * xSize] = value;
			return this;
		}
		
		public int get(int x, int z) {
			return map[x + z * xSize];
		}
		
		public TerrainMap build() {
			return new TerrainMap(this);
		}
		
	}
	
}
