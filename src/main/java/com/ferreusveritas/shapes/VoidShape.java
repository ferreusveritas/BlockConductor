package com.ferreusveritas.shapes;

import com.ferreusveritas.math.AABB;
import com.ferreusveritas.math.VecI;

import java.util.Optional;

/**
 * A shape that represents no blocks in the world.
 */
public class VoidShape implements Shape {
	
	@Override
	public Optional<AABB> getAABB() {
		return Optional.empty();
	}
	
	@Override
	public boolean isInside(VecI pos) {
		return false;
	}
	
}
