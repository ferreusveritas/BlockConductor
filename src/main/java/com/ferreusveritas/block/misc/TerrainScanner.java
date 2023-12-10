package com.ferreusveritas.block.misc;

import com.ferreusveritas.shape.Shape;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;

public class TerrainScanner {
	
	/**
	 * Scan the surface of the world and return a SurfaceMap object
	 * @param shape The shape to scan
	 * @param area The rectangular area to scan
	 * @return A SurfaceMap object containing the surface heights
	 */
	public TerrainMap scanSurface(Shape shape, AABBI area) {
		Vec3I size = area.size();
		int xOffset = area.min().x();
		int zOffset = area.min().z();
		int xSize = size.x();
		int zSize = size.z();
		int yLow = area.min().y();
		int yHigh = area.max().y();
		TerrainMap.Builder builder = new TerrainMap.Builder(xSize, zSize);
		
		for(int x = 0; x < xSize; x++) {
			for(int z = 0; z < zSize; z++) {
				int y = yHigh;
				int yFound = Integer.MIN_VALUE;
				for(; y >= yLow; y--) {
					Vec3I pos = new Vec3I(x + xOffset, y, z + zOffset);
					boolean isInside = shape.isInside(pos);
					if(isInside) {
						yFound = y;
						break;
					}
				}
				builder.set(x, z, yFound);
			}
		}
		
		return builder.build();
	}
	
}
