package com.ferreusveritas.shapes;

import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;

import java.util.Optional;

public class SphereShape implements Shape {
	
	private final Vec3I center;
	private final double radius;
	private final AABBI aabb;
	
	public SphereShape(Vec3I center, double radius) {
		this.center = center;
		this.radius = radius;
		this.aabb = new AABBI(center, center).expand((int)Math.ceil(radius)).orElseThrow();
	}
	
	@Override
	public Optional<AABBI> getAABB() {
		return Optional.of(aabb);
	}
	
	@Override
	public boolean isInside(Vec3I pos) {
		return center.distanceSqTo(pos) <= radius * radius;
	}
	
}
