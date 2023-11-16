package com.ferreusveritas.shapes;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.math.Vec3I;

import java.util.Optional;

/**
 * A vertically oriented cylinder shape
 */
public class CylinderShape implements Shape {
	
	private final Vec3D center;
	private final double radius;
	private final int h;
	
	public CylinderShape(Vec3D center, double radius, int h) {
		this.center = center;
		this.radius = radius;
		this.h = h;
	}
	
	@Override
	public Optional<AABBI> getAABB() {
		AABBD aabb = new AABBD(center.add(new Vec3D(-radius, 0, -radius)), center.add(new Vec3D(radius, h, radius)));
		return Optional.of(aabb.toAABBI());
	}
	
	@Override
	public boolean isInside(Vec3I pos) {
		Vec3D delta = pos.toVecD().sub(center);
		return delta.y() >= 0 && delta.y() < h && delta.x() * delta.x() + delta.z() * delta.z() <= radius * radius;
	}
	
}
