package com.ferreusveritas.shapes;

import com.ferreusveritas.api.AABB;
import com.ferreusveritas.api.VecI;

import java.util.List;
import java.util.Optional;

/**
 * Combine two or more shapes in such a way that only the blocks common to all are present.  This is an intersect (∩) operation
 */
public class IntersectShape implements Shape {
	
	private final List<Shape> shapes;
	
	public IntersectShape(Shape... shapes) {
		this.shapes = List.of(shapes);
		if(this.shapes.isEmpty()) {
			throw new IllegalArgumentException("IntersectShape must have at least one shape");
		}
	}
	
	@Override
	public Optional<AABB> getAABB() {
		AABB aabb = null;
		for (Shape shape : shapes) {
			if (aabb == null) {
				aabb = shape.getAABB().orElse(null);
			} else {
				AABB next = shape.getAABB().orElse(null);
				if(next == null) {
					return Optional.empty();
				}
				aabb = aabb.intersect(next).orElse(null);
				if(aabb == null) {
					return Optional.empty();
				}
			}
		}
		return Optional.ofNullable(aabb);
	}
	
	@Override
	public boolean isInside(VecI pos) {
		return false;
	}
}
