package com.ferreusveritas.shapes;

import com.ferreusveritas.api.AABB;
import com.ferreusveritas.api.VecI;

import java.util.Optional;

/**
 * Hollows out a provided shape using a 3d kernel
 */
public class CavitateShape implements Shape {
	
	private final Shape shape;
	
	public CavitateShape(Shape shape) {
		this.shape = shape;
	}
	
	@Override
	public Optional<AABB> getAABB() {
		return shape.getAABB();
	}
	
	@Override
	public boolean isInside(VecI pos) {
		return shape.isInside(pos) &&
			!(
				shape.isInside(pos.add(VecI.DOWN))
				&& shape.isInside(pos.add(VecI.UP))
				&& shape.isInside(pos.add(VecI.NORTH))
				&& shape.isInside(pos.add(VecI.SOUTH))
				&& shape.isInside(pos.add(VecI.EAST))
				&& shape.isInside(pos.add(VecI.WEST))
			);
	}
}
