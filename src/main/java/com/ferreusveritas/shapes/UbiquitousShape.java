package com.ferreusveritas.shapes;

import com.fasterxml.jackson.annotation.JsonValue;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;

import java.util.Map;
import java.util.Optional;

/**
 * A shape that represents all blocks in the world.
 */
public class UbiquitousShape implements Shape {
	
	public static final String TYPE = "ubiquitous";
	
	@Override
	public Optional<AABBI> getAABB() {
		return Optional.of(AABBI.INFINITE);
	}
	
	@JsonValue
	private Map<String, Object> getJson() {
		return Map.of(
			"type", TYPE
		);
	}
	
	@Override
	public boolean isInside(Vec3I pos) {
		return true;
	}
	
}
