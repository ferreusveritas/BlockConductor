package com.ferreusveritas.shapes;

import com.ferreusveritas.math.AABB;
import com.ferreusveritas.math.VecI;

import java.util.Optional;

public class SphereShape implements Shape {
	
	private final VecI center;
	private final double radius;
	private final AABB aabb;
	
	public SphereShape(VecI center, double radius) {
		this.center = center;
		this.radius = radius;
		this.aabb = new AABB(center, center).grow((int)Math.ceil(radius)).orElseThrow();
	}
	
	@Override
	public Optional<AABB> getAABB() {
		return Optional.of(aabb);
	}
	
	@Override
	public boolean isInside(VecI pos) {
		return center.distanceSqTo(pos) <= radius * radius;
	}
	
}
