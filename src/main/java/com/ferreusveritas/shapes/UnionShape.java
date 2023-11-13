package com.ferreusveritas.shapes;

import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;

import java.util.List;
import java.util.Optional;

/**
 * Combine two or more shape providers with a Union (|) operation
 */
public class UnionShape implements Shape {
	
	private final List<Shape> shapes;
	
	public UnionShape(Shape... shapes) {
		this.shapes = List.of(shapes);
		if(this.shapes.isEmpty()) {
			throw new IllegalArgumentException("UnionShape must have at least one shape");
		}
	}
	
	@Override
	public Optional<AABBI> getAABB() {
		AABBI aabb = null;
		for (Shape shape : shapes) {
			if (aabb == null) {
				aabb = shape.getAABB().orElse(null);
				continue;
			}
			AABBI next = shape.getAABB().orElse(null);
			if(next != null) {
				aabb = aabb.union(next);
			}
		}
		return Optional.ofNullable(aabb);
	}
	
	@Override
	public boolean isInside(Vec3I pos) {
		for (Shape shape : shapes) {
			if (shape.isInside(pos)) {
				return true;
			}
		}
		return false;
	}
}
