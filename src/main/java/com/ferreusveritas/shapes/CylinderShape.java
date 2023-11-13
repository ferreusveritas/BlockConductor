package com.ferreusveritas.shapes;

import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;

import java.util.Optional;

/**
 * A vertically oriented cylinder shape
 */
public class CylinderShape implements Shape {
	
	private final Vec3I center;
	private final double radius;
	private final int h;
	
	public CylinderShape(Vec3I center, double radius, int h) {
		this.center = center;
		this.radius = radius;
		this.h = h;
	}
	
	@Override
	public Optional<AABBI> getAABB() {
		int radius = (int) Math.ceil(this.radius);
		return Optional.of(new AABBI(center.add(new Vec3I(-radius, 0, -radius)), center.add(new Vec3I(radius, h, radius))));
	}
	
	@Override
	public boolean isInside(Vec3I pos) {
		Vec3I delta = pos.sub(center);
		return delta.y() >= 0 && delta.y() < h && delta.x() * delta.x() + delta.z() * delta.z() <= radius * radius;
	}
	
}
