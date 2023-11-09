package com.ferreusveritas.shapes;

import com.ferreusveritas.api.AABB;
import com.ferreusveritas.api.VecI;

import java.util.Optional;

/**
 * Inverts the represented shape, so it's empty where there's blocks and vice versa.
 */
public class InvertShape implements Shape {
	
	private final Shape shape;
	
	public InvertShape(Shape shape) {
		this.shape = shape;
	}
	
	@Override
	public Optional<AABB> getAABB() {
		return Optional.of(AABB.INFINITE);
	}
	
	@Override
	public boolean isInside(VecI pos) {
		return !shape.isInside(pos);
	}
}
