package com.ferreusveritas.math;

public record VecD(
	double x,
	double y,
	double z
) {
	
	public static final VecD ZERO = new VecD(0, 0, 0);
	public static final VecD DOWN = ZERO.withY(-1);
	public static final VecD UP = ZERO.withY(1);
	public static final VecD NORTH = ZERO.withZ(-1);
	public static final VecD SOUTH = ZERO.withZ(1);
	public static final VecD WEST = ZERO.withX(-1);
	public static final VecD EAST = ZERO.withX(1);
	
	public VecD(VecI vec) {
		this(vec.x() + 0.5, vec.y() + 0.5, vec.z() + 0.5);
	}
	
	public VecI toVecI() {
		return new VecI(this);
	}
	
	public VecD withX(double x) {
		return new VecD(x, y, z);
	}
	
	public VecD withY(double y) {
		return new VecD(x, y, z);
	}
	
	public VecD withZ(double z) {
		return new VecD(x, y, z);
	}
	
	public VecD neg() {
		return new VecD(-x, -y, -z);
	}
	
	public VecD add(VecD pos) {
		return new VecD(x + pos.x, y + pos.y, z + pos.z);
	}
	
	public VecD sub(VecD pos) {
		return new VecD(x - pos.x, y - pos.y, z - pos.z);
	}
	
	public VecD mul(double scale) {
		return new VecD(x * scale, y * scale, z * scale);
	}
	
	public VecD div(double scale) {
		return new VecD(x / scale, y / scale, z / scale);
	}
	
	public double distanceTo(VecD center) {
		return Math.sqrt(distanceSqTo(center));
	}
	
	public double distanceSqTo(VecD center) {
		double dx = x - center.x;
		double dy = y - center.y;
		double dz = z - center.z;
		return dx * dx + dy * dy + dz * dz;
	}
	
	public double lenSq() {
		return x * x + y * y + z * z;
	}
	
	public double len() {
		return Math.sqrt(lenSq());
	}
	
	public VecD normalize() {
		return div(len());
	}
	
	public VecD cross(VecD pos) {
		return new VecD(y * pos.z - z * pos.y, z * pos.x - x * pos.z, x * pos.y - y * pos.x);
	}
	
	public double dot(VecD pos) {
		return x * pos.x + y * pos.y + z * pos.z;
	}
	
}
