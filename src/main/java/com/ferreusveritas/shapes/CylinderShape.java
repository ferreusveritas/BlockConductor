package com.ferreusveritas.shapes;

import com.ferreusveritas.math.AABB;
import com.ferreusveritas.math.VecI;

import java.util.Optional;

/**
 * A vertically oriented cylinder shape
 */
public class CylinderShape implements Shape {
	
	private final VecI center;
	private final double radius;
	private final int h;
	
	public CylinderShape(VecI center, double radius, int h) {
		this.center = center;
		this.radius = radius;
		this.h = h;
	}
	
	@Override
	public Optional<AABB> getAABB() {
		int radius = (int) Math.ceil(this.radius);
		return Optional.of(new AABB(center.add(new VecI(-radius, 0, -radius)), center.add(new VecI(radius, h, radius))));
	}
	
	@Override
	public boolean isInside(VecI pos) {
		VecI delta = pos.sub(center);
		return delta.y() >= 0 && delta.y() < h && delta.x() * delta.x() + delta.z() * delta.z() <= radius * radius;
	}
	
}
