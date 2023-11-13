package com.ferreusveritas.shapes;

import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;

import java.util.Optional;

/**
 * A shape that represents a layer of blocks in the y axis.
 */
public class LayerShape implements Shape {
	
	private final int minY;
	private final int maxY;
	
	public LayerShape(int minY, int maxY) {
		this.minY = minY;
		this.maxY = maxY;
	}
	
	@Override
	public Optional<AABBI> getAABB() {
		return Optional.of(new AABBI(Integer.MIN_VALUE, minY, Integer.MIN_VALUE, Integer.MAX_VALUE, maxY, Integer.MAX_VALUE));
	}
	
	@Override
	public boolean isInside(Vec3I pos) {
		return pos.y() >= minY && pos.y() <= maxY;
	}
	
}
