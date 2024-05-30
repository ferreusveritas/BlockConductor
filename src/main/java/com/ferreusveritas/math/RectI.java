package com.ferreusveritas.math;

public record RectI(
	int x1, //inclusive
	int z1, //inclusive
	int x2, //inclusive
	int z2  //inclusive
) {
	
	public static final RectI INFINITE = new RectI(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
	public static final RectI EMPTY = new RectI(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
	
	public RectI(Vec3I size) {
		this(size.x(), size.z());
	}
	
	public RectI(int width, int height) {
		this(0, 0, width - 1, height - 1);
	}
	
	public RectI(AABBI aabb) {
		this(
			aabb.min().x(),
			aabb.min().z(),
			aabb.max().x(),
			aabb.max().z()
		);
	}
	
	public boolean contains(Vec3I vec) {
		return contains(vec.x(), vec.y());
	}
	
	public boolean contains(int x, int z) {
		if(this == INFINITE) {
			return true;
		}
		if(this == EMPTY) {
			return false;
		}
		return x >= x1 && x <= x2 && z >= z1 && z <= z2;
	}
	
	public int width() {
		if(this == EMPTY) {
			return 0;
		}
		if(x1 == Integer.MIN_VALUE || x2 == Integer.MAX_VALUE) {
			return Integer.MAX_VALUE;
		}
		return x2 - x1 + 1;
	}
	
	public int height() {
		if(this == EMPTY) {
			return 0;
		}
		if(z1 == Integer.MIN_VALUE || z2 == Integer.MAX_VALUE) {
			return Integer.MAX_VALUE;
		}
		return z2 - z1 + 1;
	}
	
	public RectI offset(int x, int z) {
		if(this == INFINITE || this == EMPTY) {
			return this;
		}
		int x1n = x1;
		int z1n = z1;
		int x2n = x2;
		int z2n = z2;
		if(x1n != Integer.MIN_VALUE && x1n != Integer.MAX_VALUE) {
			x1n += x;
		}
		if(z1n != Integer.MIN_VALUE && z1n != Integer.MAX_VALUE) {
			z1n += z;
		}
		if(x2n != Integer.MIN_VALUE && x2n != Integer.MAX_VALUE) {
			x2n += x;
		}
		if(z2n != Integer.MIN_VALUE && z2n != Integer.MAX_VALUE) {
			z2n += z;
		}
		return new RectI(x1n, z1n, x2n, z2n);
	}
	
	public RectI intersect(RectI o) {
		if(this == INFINITE) {
			return o;
		}
		if(o == INFINITE) {
			return this;
		}
		if(this == EMPTY || o == EMPTY) {
			return EMPTY;
		}
		return new RectI(
			Math.max(x1, o.x1),
			Math.max(z1, o.z1),
			Math.min(x2, o.x2),
			Math.min(z2, o.z2)
		);
	}
	
	public RectI union(RectI o) {
		if(this == INFINITE || o == INFINITE) {
			return INFINITE;
		}
		if(this == EMPTY) {
			return o;
		}
		if(o == EMPTY) {
			return this;
		}
		return new RectI(
			Math.min(x1, o.x1),
			Math.min(z1, o.z1),
			Math.max(x2, o.x2),
			Math.max(z2, o.z2)
		);
	}
	
	public static RectI union(RectI a, RectI b) {
		if(a == INFINITE || b == INFINITE) {
			return INFINITE;
		}
		if(a == EMPTY) {
			return b;
		}
		if(b == EMPTY) {
			return a;
		}
		return a.union(b);
	}
	
}
