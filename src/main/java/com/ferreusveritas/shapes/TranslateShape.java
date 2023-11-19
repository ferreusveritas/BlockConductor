package com.ferreusveritas.shapes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;

import java.util.Map;
import java.util.Optional;

/**
 * Offset a shape by a specified amount.
 */
public class TranslateShape implements Shape {
	
	public static final String TYPE = "translate";
	
	private final Shape shape;
	private final Vec3I offset;
	
	@JsonCreator
	public TranslateShape(
		@JsonProperty("shape") Shape shape,
		@JsonProperty("offset") Vec3I offset
	) {
		this.shape = shape;
		this.offset = offset;
	}
	
	@JsonValue
	private Map<String, Object> getJson() {
		return Map.of(
			"type", TYPE,
			"shape", shape,
			"offset", offset
		);
	}
	
	@Override
	public Optional<AABBI> getAABB() {
		return shape.getAABB().map(aabb -> aabb.offset(offset));
	}
	
	@Override
	public boolean isInside(Vec3I pos) {
		return shape.isInside(pos.sub(offset));
	}
	
}
