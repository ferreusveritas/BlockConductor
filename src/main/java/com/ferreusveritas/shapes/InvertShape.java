package com.ferreusveritas.shapes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;

import java.util.Optional;

/**
 * Inverts the represented shape, so it's empty where there's blocks and vice versa.
 */
public class InvertShape implements Shape {
	
	private final Shape shape;
	
	@JsonCreator
	public InvertShape(
		@JsonProperty("shape") Shape shape
	) {
		this.shape = shape;
	}
	
	@Override
	public Optional<AABBI> getAABB() {
		return Optional.of(AABBI.INFINITE);
	}
	
	@Override
	public boolean isInside(Vec3I pos) {
		return !shape.isInside(pos);
	}
}
