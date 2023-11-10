package com.ferreusveritas.shapes;

import com.ferreusveritas.math.AABB;
import com.ferreusveritas.math.VecI;

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
	public Optional<AABB> getAABB() {
		return Optional.of(new AABB(Integer.MIN_VALUE, minY, Integer.MIN_VALUE, Integer.MAX_VALUE, maxY, Integer.MAX_VALUE));
	}
	
	@Override
	public boolean isInside(VecI pos) {
		return pos.y() >= minY && pos.y() <= maxY;
	}
	
}
