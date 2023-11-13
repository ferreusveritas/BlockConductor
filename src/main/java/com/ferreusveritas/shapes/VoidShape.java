package com.ferreusveritas.shapes;

import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;

import java.util.Optional;

/**
 * A shape that represents no blocks in the world.
 */
public class VoidShape implements Shape {
	
	@Override
	public Optional<AABBI> getAABB() {
		return Optional.empty();
	}
	
	@Override
	public boolean isInside(Vec3I pos) {
		return false;
	}
	
}
