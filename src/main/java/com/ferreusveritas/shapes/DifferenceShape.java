package com.ferreusveritas.shapes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;

import java.util.Map;
import java.util.Optional;

/**
 * Remove the presence of the second shape from the first shape with a Difference (-) operation
 */
public class DifferenceShape implements Shape {
	
	public static final String TYPE = "difference";
	
	private final Shape shape1;
	private final Shape shape2;
	
	@JsonCreator
	public DifferenceShape(
		@JsonProperty("shape1") Shape shape1,
		@JsonProperty("shape2") Shape shape2
	) {
		this.shape1 = shape1;
		this.shape2 = shape2;
	}
	
	@JsonValue
	private Object getJson() {
		return Map.of(
			"type", TYPE,
			"shape1", shape1,
			"shape2", shape2
		);
	}
	
	@Override
	public Optional<AABBI> getAABB() {
		return shape1.getAABB();
	}
	
	@Override
	public boolean isInside(Vec3I pos) {
		return shape1.isInside(pos) && !shape2.isInside(pos);
	}
	
}
