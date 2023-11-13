package com.ferreusveritas.math;

public record Vec2D(
	double x,
	double z // you can thank Mojang for this
) {
	
	public static final Vec2D ZERO = new Vec2D(0, 0);
	public static final Vec2D NORTH = ZERO.withZ(-1);
	public static final Vec2D SOUTH = ZERO.withZ(1);
	public static final Vec2D WEST = ZERO.withX(-1);
	public static final Vec2D EAST = ZERO.withX(1);
	
	public Vec2D(Vec3I vec) {
		this(vec.x() + 0.5, vec.z() + 0.5);
	}
	
	public Vec2D(Vec3D vec) {
		this(vec.x(), vec.z());
	}
	
	public Vec3D toVecD() {
		return new Vec3D(x, 0, z);
	}
	
	public Vec2D neg() {
		return new Vec2D(-x, -z);
	}
	
	public Vec2D withX(double x) {
		return new Vec2D(x, z);
	}
	
	public Vec2D withZ(double z) {
		return new Vec2D(x, z);
	}
	
	public Vec2D add(Vec2D pos) {
		return new Vec2D(x + pos.x, z + pos.z);
	}
	
	public Vec2D sub(Vec2D pos) {
		return new Vec2D(x - pos.x, z - pos.z);
	}
	
	public Vec2D scale(double scale) {
		return new Vec2D(x * scale, z * scale);
	}
	
	public Vec2D div(double scale) {
		return new Vec2D(x / scale, z / scale);
	}
	
	public double distanceTo(Vec2D center) {
		return Math.sqrt(distanceSqTo(center));
	}
	
	public double distanceSqTo(Vec2D p) {
		double dx = x - p.x;
		double dz = z - p.z;
		return dx * dx + dz * dz;
	}
	
}
