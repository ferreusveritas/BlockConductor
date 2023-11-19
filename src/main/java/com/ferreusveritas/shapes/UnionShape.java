package com.ferreusveritas.shapes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Combine two or more shape providers with a Union (|) operation
 */
public class UnionShape implements Shape {
	
	public static final String TYPE = "union";
	
	private final List<Shape> shapes;
	
	@JsonCreator
	public UnionShape(
		@JsonProperty("shapes") Shape... shapes
	) {
		this.shapes = List.of(shapes);
		if(this.shapes.isEmpty()) {
			throw new IllegalArgumentException("UnionShape must have at least one shape");
		}
	}
	
	@JsonValue
	private Map<String, Object> getJson() {
		return Map.of(
			"type", TYPE,
			"shapes", shapes
		);
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
