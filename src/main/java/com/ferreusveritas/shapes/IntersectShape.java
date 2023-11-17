package com.ferreusveritas.shapes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;

import java.util.List;
import java.util.Optional;

/**
 * Combine two or more shapes in such a way that only the blocks common to all are present.  This is an intersect (∩) operation
 */
public class IntersectShape implements Shape {
	
	private final List<Shape> shapes;
	
	@JsonCreator
	public IntersectShape(
		@JsonProperty("shapes") Shape... shapes
	) {
		this.shapes = List.of(shapes);
		if(this.shapes.isEmpty()) {
			throw new IllegalArgumentException("IntersectShape must have at least one shape");
		}
	}
	
	@Override
	public Optional<AABBI> getAABB() {
		AABBI aabb = null;
		for (Shape shape : shapes) {
			if (aabb == null) {
				aabb = shape.getAABB().orElse(null);
			} else {
				AABBI next = shape.getAABB().orElse(null);
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
	public boolean isInside(Vec3I pos) {
		return false;
	}
}
