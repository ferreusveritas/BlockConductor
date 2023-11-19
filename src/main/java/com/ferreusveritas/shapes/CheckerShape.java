package com.ferreusveritas.shapes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Axis;
import com.ferreusveritas.math.Vec3I;

import java.util.Map;
import java.util.Optional;

public class CheckerShape implements Shape {
	
	public static final String TYPE = "checker";
	
	private final Axis axis; // null means all axes(3D checker block)
	
	public CheckerShape() {
		this(null);
	}
	
	@JsonCreator
	public CheckerShape(
		@JsonProperty("axis") Axis axis
	) {
		this.axis = axis;
	}
	
	@JsonValue
	private Map<String, Object> getJson() {
		return Map.of(
			"type", TYPE,
			"axis", axis
		);
	}
	
	@Override
	public Optional<AABBI> getAABB() {
		return Optional.of(AABBI.INFINITE);
	}
	
	@Override
	public boolean isInside(Vec3I pos) {
		if(axis == null) {
			return (pos.x() + pos.y() + pos.z()) % 2 == 0;
		}
		return switch (axis) {
			case X -> pos.y() + pos.z() % 2 == 0;
			case Y -> pos.x() + pos.z() % 2 == 0;
			case Z -> pos.x() + pos.y() % 2 == 0;
		};
		
	}
	
}
