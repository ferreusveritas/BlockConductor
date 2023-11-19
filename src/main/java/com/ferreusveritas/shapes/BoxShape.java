package com.ferreusveritas.shapes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;

import java.util.Map;
import java.util.Optional;

public class BoxShape implements Shape {
	
	public static final String TYPE = "box";
	
	private final AABBI aabb;
	
	@JsonCreator
	public BoxShape(
		@JsonProperty("aabb") AABBI aabb
	) {
		this.aabb = aabb;
	}
	
	@JsonValue
	private Map<String, Object> getJson() {
		return Map.of(
			"type", TYPE,
			"aabb", aabb
		);
	}
	
	@Override
	public Optional<AABBI> getAABB() {
		return Optional.of(aabb);
	}
	
	@Override
	public boolean isInside(Vec3I pos) {
		return aabb.isPointInside(pos);
	}
	
}
