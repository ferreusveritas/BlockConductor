package com.ferreusveritas.math;

public record RectI(
	int x1,
	int z1,
	int x2,
	int z2
) {
	
	public static final RectI INFINITE = new RectI(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
	
	public RectI(Vec3I size) {
		this(size.x(), size.z());
	}
	
	public RectI(int width, int height) {
		this(0, 0, width - 1, height - 1);
	}
	
	public boolean isInside(Vec3I vec) {
		return isInside(vec.x(), vec.y());
	}
	
	public boolean isInside(int x, int z) {
		return x >= x1 && x < x2 && z >= z1 && z < z2;
	}
	
	public int width() {
		return x2 - x1 + 1;
	}
	
	public int height() {
		return z2 - z1 + 1;
	}
	
	public RectI offset(int x, int z) {
		return new RectI(x1 + x, z1 + z, x2 + x, z2 + z);
	}
	
	public RectI intersect(RectI o) {
		return new RectI(
			Math.max(x1, o.x1),
			Math.max(z1, o.z1),
			Math.min(x2, o.x2),
			Math.min(z2, o.z2)
		);
	}
	
}
