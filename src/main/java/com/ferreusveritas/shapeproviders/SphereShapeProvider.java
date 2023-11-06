package com.ferreusveritas.shapeproviders;

import com.ferreusveritas.api.AABB;
import com.ferreusveritas.api.VecI;

public class SphereShapeProvider implements ShapeProvider {
	
	private final VecI center;
	private final int radius;
	private final AABB aabb;
	
	public SphereShapeProvider(VecI center, int radius) {
		this.center = center;
		this.radius = radius;
		this.aabb = new AABB(center, center).grow(radius).orElseThrow();
	}
	
	@Override
	public AABB getAABB() {
		return aabb;
	}
	
	@Override
	public boolean isInside(VecI pos) {
		return center.distanceSqTo(pos) <= radius * radius;
	}
	
}
