package com.ferreusveritas.math;

import com.fasterxml.jackson.annotation.JsonCreator;

public record RectD(
	double x1, //inclusive
	double z1, //inclusive
	double x2, //exclusive
	double z2  //exclusive
) {
	
	public static final RectD INFINITE = new RectD(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
	public static final RectD EMPTY = new RectD(Double.NaN, Double.NaN, Double.NaN, Double.NaN);
	
	@JsonCreator
	public static RectD create(double x1, double z1, double x2, double z2) {
		return new RectD(x1, z1, x2, z2).resolve();
	}
	
	public static RectD create(Vec2D min, Vec2D max) {
		return new RectD(min.x(), min.z(), max.x(), max.z()).resolve();
	}
	
	public static RectD create(Vec2D size) {
		return new RectD(0, 0, size.x(), size.z()).resolve();
	}
	
	public static RectD create(double width, double height) {
		return new RectD(0, 0, width, height).resolve();
	}
	
	public static RectD create(AABBD aabb) {
		return new RectD(aabb.min().x(), aabb.min().z(), aabb.max().x(), aabb.max().z()).resolve();
	}
	
	public AABBD toAABB() {
		if(this == INFINITE) {
			return AABBD.INFINITE;
		}
		if(this == EMPTY) {
			return AABBD.EMPTY;
		}
		return new AABBD(this).resolve();
	}
	
	public RectD offset(double x, double z) {
		if(this == INFINITE || this == EMPTY) {
			return this;
		}
		return new RectD(x1 + x, z1 + z, x2 + x, z2 + z);
	}
	
	public Vec3D size() {
		if(this == INFINITE) {
			return Vec3D.INFINITY.withY(0);
		}
		if(this == EMPTY) {
			return Vec3D.ZERO;
		}
		return new Vec3D(x2 - x1, 0, z2 - z1);
	}
	
	public boolean contains(double x, double z) {
		if(this == INFINITE) {
			return true;
		}
		if(this == EMPTY) {
			return false;
		}
		return x >= x1 && x < x2 && z >= z1 && z < z2;
	}
	
	public boolean contains(Vec2D vec) {
		return contains(vec.x(), vec.z());
	}
	
	public boolean intersects(RectD o) {
		if(this == INFINITE || o == INFINITE) {
			return true;
		}
		if(this == EMPTY || o == EMPTY) {
			return false;
		}
		return x1 < o.x2 && x2 > o.x1 && z1 < o.z2 && z2 > o.z1;
	}
	
	public RectD union(RectD o) {
		if(this == INFINITE || o == INFINITE) {
			return INFINITE;
		}
		if(this == EMPTY) {
			return o;
		}
		if(o == EMPTY) {
			return this;
		}
		return new RectD(
			Math.min(x1, o.x1),
			Math.min(z1, o.z1),
			Math.max(x2, o.x2),
			Math.max(z2, o.z2)
		);
	}
	
	public RectD intersect(RectD o) {
		if(this == INFINITE) {
			return o;
		}
		if(o == INFINITE) {
			return this;
		}
		if(this == EMPTY || o == EMPTY) {
			return EMPTY;
		}
		return new RectD(
			Math.max(x1, o.x1),
			Math.max(z1, o.z1),
			Math.min(x2, o.x2),
			Math.min(z2, o.z2)
		);
	}
	
	public RectD[] subdivide() {
		if(this == INFINITE) {
			throw new UnsupportedOperationException("Cannot subdivide infinite RectD");
		}
		if(this == EMPTY) {
			return new RectD[] { EMPTY, EMPTY, EMPTY, EMPTY };
		}
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
		if(this == INFINITE) {
			throw new UnsupportedOperationException("Cannot get vertices of infinite RectD");
		}
		if(this == EMPTY) {
			throw new UnsupportedOperationException("Cannot get vertices of empty RectD");
		}
		return new Vec2D[] {
			new Vec2D(x1, z1),
			new Vec2D(x2, z1),
			new Vec2D(x2, z2),
			new Vec2D(x1, z2)
		};
	}
	
	public RectD resolve() {
		if(this == INFINITE || this == EMPTY) {
			return this;
		}
		if(
			Double.isNaN(x1) ||
			Double.isNaN(z1) ||
			Double.isNaN(x2) ||
			Double.isNaN(z2)
		) {
			return EMPTY;
		}
		return
			Double.NEGATIVE_INFINITY != x1 ||
			Double.NEGATIVE_INFINITY != z1 ||
			Double.POSITIVE_INFINITY != x2 ||
			Double.POSITIVE_INFINITY != z2
		? this
		: INFINITE;
	}
	
}
