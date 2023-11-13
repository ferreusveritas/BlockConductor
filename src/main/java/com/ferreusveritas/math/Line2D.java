package com.ferreusveritas.math;

public record Line2D(
	Vec2D start,
	Vec2D end
) {
	
	public double length() {
		return start.distanceTo(end);
	}
	
}
