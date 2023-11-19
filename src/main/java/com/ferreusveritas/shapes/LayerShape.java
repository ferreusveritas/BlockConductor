package com.ferreusveritas.shapes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;

import java.util.Map;
import java.util.Optional;

/**
 * A shape that represents a layer of blocks in the y axis.
 */
public class LayerShape implements Shape {
	
	public static final String TYPE = "layer";
	
	private final int min;
	private final int max;
	
	@JsonCreator
	public LayerShape(
		@JsonProperty("min") int min,
		@JsonProperty("max") int max
	) {
		this.min = min;
		this.max = max;
	}
	
	@JsonValue
	private Object getJson() {
		return Map.of(
			"type", TYPE,
			"min", min,
			"max", max
		);
	}
	
	@Override
	public Optional<AABBI> getAABB() {
		return Optional.of(new AABBI(Integer.MIN_VALUE, min, Integer.MIN_VALUE, Integer.MAX_VALUE, max, Integer.MAX_VALUE));
	}
	
	@Override
	public boolean isInside(Vec3I pos) {
		return pos.y() >= min && pos.y() <= max;
	}
	
}
