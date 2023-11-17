package com.ferreusveritas.shapes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.math.Vec3I;

import java.util.Optional;

public class SphereShape implements Shape {
	
	private final Vec3D center;
	private final double radius;
	private final AABBI aabb;
	
	@JsonCreator
	public SphereShape(
		@JsonProperty("center") Vec3D center,
		@JsonProperty("radius") double radius
	) {
		this.center = center;
		this.radius = radius;
		Vec3I v = center.toVecI();
		this.aabb = new AABBI(v, v).expand((int)Math.ceil(radius)).orElseThrow();
	}
	
	@Override
	public Optional<AABBI> getAABB() {
		return Optional.of(aabb);
	}
	
	@Override
	public boolean isInside(Vec3I pos) {
		return center.distanceSqTo(pos.toVecD()) <= radius * radius;
	}
	
}
