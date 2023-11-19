package com.ferreusveritas.shapes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;

import java.util.Map;
import java.util.Optional;

/**
 * Hollows out a provided shape using a 3d kernel
 */
public class CavitateShape implements Shape {
	
	public static final String TYPE = "cavitate";
	
	private final Shape shape;
	
	@JsonCreator
	public CavitateShape(
		@JsonProperty("shape") Shape shape
	) {
		this.shape = shape;
	}
	
	@JsonValue
	private Map<String, Object> getJson() {
		return Map.of(
			"type", TYPE,
			"shape", shape
		);
	}
	
	@Override
	public Optional<AABBI> getAABB() {
		return shape.getAABB();
	}
	
	@Override
	public boolean isInside(Vec3I pos) {
		return shape.isInside(pos) &&
			!(
				shape.isInside(pos.add(Vec3I.DOWN))
				&& shape.isInside(pos.add(Vec3I.UP))
				&& shape.isInside(pos.add(Vec3I.NORTH))
				&& shape.isInside(pos.add(Vec3I.SOUTH))
				&& shape.isInside(pos.add(Vec3I.EAST))
				&& shape.isInside(pos.add(Vec3I.WEST))
			);
	}
}
