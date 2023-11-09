package com.ferreusveritas.shapes;

import com.ferreusveritas.api.AABB;
import com.ferreusveritas.api.VecI;

import java.util.Optional;

public class SphereShape implements Shape {
	
	private final VecI center;
	private final int radius;
	private final AABB aabb;
	
	public SphereShape(VecI center, int radius) {
		this.center = center;
		this.radius = radius;
		this.aabb = new AABB(center, center).grow(radius).orElseThrow();
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
