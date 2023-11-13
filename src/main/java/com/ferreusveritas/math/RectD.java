package com.ferreusveritas.math;

public record RectD(
	double x1,
	double z1,
	double x2,
	double z2
) {
	
	public RectD(Vec3D size) {
		this(size.x(), size.z());
	}
	
	public RectD(double width, double height) {
		this(0, 0, width, height);
	}
	
	public RectD(AABBD aabb) {
		this(aabb.min().x(), aabb.min().z(), aabb.max().x(), aabb.max().z());
	}
	
	public AABBD toAABB() {
		return new AABBD(this);
	}
	
	public RectD offset(double x, double z) {
		return new RectD(x1 + x, z1 + z, x2 + x, z2 + z);
	}
	
	public Vec3D size() {
		return new Vec3D(x2 - x1, 0, z2 - z1);
	}
	
	public boolean isInside(double x, double z) {
		return x >= x1 && x < x2 && z >= z1 && z < z2;
	}
	
	public boolean isInside(Vec2D vec) {
		return isInside(vec.x(), vec.z());
	}
	
	public boolean intersects(RectD o) {
		return x1 < o.x2 && x2 > o.x1 && z1 < o.z2 && z2 > o.z1;
	}
	
	public RectD union(RectD o) {
		return new RectD(
			Math.min(x1, o.x1),
			Math.min(z1, o.z1),
			Math.max(x2, o.x2),
			Math.max(z2, o.z2)
		);
	}
	
	public RectD intersect(RectD o) {
		return new RectD(
			Math.max(x1, o.x1),
			Math.max(z1, o.z1),
			Math.min(x2, o.x2),
			Math.min(z2, o.z2)
		);
	}
	
	public RectD[] subdivide() {
		double xMid = (x1 + x2) / 2;
		double zMid = (z1 + z2) / 2;
		return new RectD[] {
			new RectD(x1, z1, xMid, zMid),
			new RectD(xMid, z1, x2, zMid),
			new RectD(x1, zMid, xMid, z2),
			new RectD(xMid, zMid, x2, z2)
		};
	}
	
	public Vec2D[] vertices() {
		return new Vec2D[] {
			new Vec2D(x1, z1),
			new Vec2D(x2, z1),
			new Vec2D(x2, z2),
			new Vec2D(x1, z2)
		};
	}
	
}
