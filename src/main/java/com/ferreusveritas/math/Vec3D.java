package com.ferreusveritas.math;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record Vec3D(
	double x,
	double y,
	double z
) {
	
	public static final double EPSILON = 0.0000000001;
	public static final Vec3D ZERO = new Vec3D(0);
	public static final Vec3D ONE = new Vec3D(1);
	public static final Vec3D INFINITY = new Vec3D(Double.POSITIVE_INFINITY);
	public static final Vec3D NEG_INFINITY = new Vec3D(Double.NEGATIVE_INFINITY);
	public static final Vec3D NAN = new Vec3D(Double.NaN);
	public static final Vec3D DOWN = ZERO.withY(-1);
	public static final Vec3D UP = ZERO.withY(1);
	public static final Vec3D NORTH = ZERO.withZ(-1);
	public static final Vec3D SOUTH = ZERO.withZ(1);
	public static final Vec3D WEST = ZERO.withX(-1);
	public static final Vec3D EAST = ZERO.withX(1);
	
	public Vec3D(double value) {
		this(value, value, value);
	}
	
	public Vec3D(Vec3I vec) {
		this(vec.x() + 0.5, vec.y() + 0.5, vec.z() + 0.5);
	}
	
	public Vec3I toVecI() {
		return new Vec3I(this);
	}
	
	public Vec2D toVec2D() {
		return new Vec2D(x, z);
	}
	
	public Vec3D withX(double x) {
		return new Vec3D(x, y, z);
	}
	
	public Vec3D withY(double y) {
		return new Vec3D(x, y, z);
	}
	
	public Vec3D withZ(double z) {
		return new Vec3D(x, y, z);
	}
	
	public Vec3D neg() {
		return new Vec3D(-x, -y, -z);
	}
	
	public Vec3D add(Vec3D pos) {
		return new Vec3D(x + pos.x, y + pos.y, z + pos.z);
	}
	
	public Vec3D sub(Vec3D pos) {
		return new Vec3D(x - pos.x, y - pos.y, z - pos.z);
	}
	
	public Vec3D mul(double scale) {
		return new Vec3D(x * scale, y * scale, z * scale);
	}
	
	public Vec3D div(double scale) {
		return new Vec3D(x / scale, y / scale, z / scale);
	}
	
	public double distanceTo(Vec3D center) {
		return Math.sqrt(distanceSqTo(center));
	}
	
	public double distanceSqTo(Vec3D center) {
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
	
	public Vec3D normalize() {
		return div(len());
	}
	
	public Vec3D cross(Vec3D pos) {
		return new Vec3D(y * pos.z - z * pos.y, z * pos.x - x * pos.z, x * pos.y - y * pos.x);
	}
	
	public double dot(Vec3D pos) {
		return x * pos.x + y * pos.y + z * pos.z;
	}
	
	public Vec3D floor() {
		return new Vec3D(Math.floor(x), Math.floor(y), Math.floor(z));
	}
	
	public Vec3D ceil() {
		return new Vec3D(Math.ceil(x), Math.ceil(y), Math.ceil(z));
	}
	
	public Vec3D down() {
		return down(1);
	}
	
	public Vec3D down(double amount) {
		return new Vec3D(x, y - amount, z);
	}
	
	public Vec3D up() {
		return up(1);
	}
	
	public Vec3D up(double amount) {
		return new Vec3D(x, y + amount, z);
	}
	
	public Vec3D north() {
		return north(1);
	}
	
	public Vec3D north(double amount) {
		return new Vec3D(x, y, z - amount);
	}
	
	public Vec3D south() {
		return south(1);
	}
	
	public Vec3D south(double amount) {
		return new Vec3D(x, y, z + amount);
	}
	
	public Vec3D west() {
		return west(1);
	}
	
	public Vec3D west(double amount) {
		return new Vec3D(x - amount, y, z);
	}
	
	public Vec3D east() {
		return east(1);
	}
	
	public Vec3D east(double amount) {
		return new Vec3D(x + amount, y, z);
	}
	
	@JsonIgnore
	public boolean isZero() {
		return x == 0 && y == 0 && z == 0;
	}
	
	public boolean equals(Object o) {
		if (this == o){
			return true;
		}
		if (o == null) {
			return false;
		}
		return o instanceof Vec3D pos && equalsEpsilon(pos);
	}
	
	public boolean equalsEpsilon(Vec3D pos, double epsilon) {
		return Math.abs(x - pos.x) < epsilon && Math.abs(y - pos.y) < epsilon && Math.abs(z - pos.z) < epsilon;
	}
	
	public boolean equalsEpsilon(Vec3D pos) {
		return equalsEpsilon(pos, EPSILON);
	}
	
	public Vec3D resolve() {
		if(this == INFINITY || this == NEG_INFINITY || this == NAN || this == ZERO) {
			return this;
		}
		if(Double.isInfinite(x) && Double.isInfinite(y) && Double.isInfinite(z)) {
			if(x > 0 && y > 0 && z > 0) {
				return INFINITY;
			}
			if(x < 0 && y < 0 && z < 0) {
				return NEG_INFINITY;
			}
			return this;
		}
		if(Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(z)) {
			return NAN;
		}
		if(x == 0 && y == 0 && z == 0) {
			return ZERO;
		}
		return this;
	}
	
}
