package com.ferreusveritas.shapes;

import com.ferreusveritas.math.AABB;
import com.ferreusveritas.math.VecI;

import java.util.Optional;

/**
 * A shape that represents all blocks in the world.
 */
public class UbiqueShape implements Shape {

	public UbiqueShape(Shape shape) {}
	
	@Override
	public Optional<AABB> getAABB() {
		return Optional.of(AABB.INFINITE);
	}
	
	@Override
	public boolean isInside(VecI pos) {
		return true;
	}
	
}