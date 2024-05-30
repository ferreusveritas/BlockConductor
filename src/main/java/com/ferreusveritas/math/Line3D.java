package com.ferreusveritas.math;

public record Line3D(
	Vec3D start,
	Vec3D end
) {
	
	public double length() {
		return start.distanceTo(end);
	}
	
	public Vec3D vec3D() {
		return end.sub(start);
	}
	
	public Line3D offset(Vec3D v) {
		return new Line3D(start.add(v), end.add(v));
	}
	
	public Line3D flip() {
		return new Line3D(end, start);
	}
	
}
