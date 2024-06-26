package com.ferreusveritas.math;

public record AABBD(
	Vec3D min, // inclusive
	Vec3D max // exclusive
) {
	
	public static AABBD INFINITE = new AABBD(Vec3D.NEG_INFINITY, Vec3D.INFINITY);
	public static AABBD EMPTY = new AABBD(Vec3D.NAN, Vec3D.NAN);
	
	public AABBD(AABBI aabb) {
		this(aabb.min().toVecD(), aabb.max().toVecD());
	}
	
	public AABBD(Vec3I min, Vec3I max) {
		this(min.toVecD().floor(), max.toVecD().ceil());
	}
	
	public AABBD(RectD rect) {
		this(
			new Vec3D(rect.x1(), Double.NEGATIVE_INFINITY, rect.z1()).resolve(),
			new Vec3D(rect.x2(), Double.POSITIVE_INFINITY, rect.z2()).resolve()
		);
	}
	
	public AABBI toAABBI() {
		if(this == INFINITE) {
			return AABBI.INFINITE;
		}
		if(this == EMPTY) {
			return AABBI.EMPTY;
		}
		return new AABBI(this);
	}
	
	public RectD toRect() {
		return RectD.create(this);
	}
	
	public AABBD offset(Vec3D offset) {
		if(this == INFINITE) {
			return this;
		}
		if(this == EMPTY) {
			return this;
		}
		return new AABBD(min.add(offset), max.add(offset));
	}
	
	public AABBD expand(double amount) {
		if(this == INFINITE) {
			return this;
		}
		if(this == EMPTY) {
			return this;
		}
		return new AABBD(
			min.sub(new Vec3D(amount)),
			max.add(new Vec3D(amount))
		);
	}

	public AABBD expand(Vec3D amount) {
		if(this == INFINITE) {
			return this;
		}
		if(this == EMPTY) {
			return this;
		}
		return new AABBD(min.sub(amount), max.add(amount));
	}

	public AABBD shrink(double amount) {
		return expand(-amount);
	}

	public AABBD shrink(Vec3D amount) {
		return expand(amount.neg());
	}
	
	public boolean contains(Vec3D pos) {
		if(this == INFINITE) {
			return true;
		}
		if(this == EMPTY) {
			return false;
		}
		return
			pos.x() >= min.x() && pos.x() < max.x() &&
			pos.y() >= min.y() && pos.y() < max.y() &&
			pos.z() >= min.z() && pos.z() < max.z();
	}
	
	public boolean intersects(AABBD other) {
		if(this == INFINITE) {
			return true;
		}
		if(this == EMPTY) {
			return false;
		}
		return
			min.x() < other.max.x() && max.x() > other.min.x() &&
			min.y() < other.max.y() && max.y() > other.min.y() &&
			min.z() < other.max.z() && max.z() > other.min.z();
	}
	
	public AABBD union(AABBD other) {
		if(this == INFINITE || other == INFINITE) {
			return INFINITE;
		}
		if(this == EMPTY) {
			return other;
		}
		if(other == EMPTY) {
			return this;
		}
		return new AABBD(
			new Vec3D(
				Math.min(min.x(), other.min.x()),
				Math.min(min.y(), other.min.y()),
				Math.min(min.z(), other.min.z())
			),
			new Vec3D(
				Math.max(max.x(), other.max.x()),
				Math.max(max.y(), other.max.y()),
				Math.max(max.z(), other.max.z())
			)
		);
	}
	
	public static AABBD union(AABBD ... aabbs) {
		AABBD aabb = EMPTY;
		for (AABBD a : aabbs) {
			aabb = union(aabb, a);
		}
		return aabb;
	}
	
	public static AABBD union(AABBD a, AABBD b) {
		if(a != EMPTY && b != EMPTY) {
			return a.union(b);
		}
		return a != EMPTY ? a : b;
	}
	
	public AABBD intersect(AABBD other) {
		if(this == INFINITE) {
			return other;
		}
		if(other == INFINITE) {
			return this;
		}
		if(this == EMPTY || other == EMPTY) {
			return EMPTY;
		}
		return new AABBD(
			new Vec3D(
				Math.max(min.x(), other.min.x()),
				Math.max(min.y(), other.min.y()),
				Math.max(min.z(), other.min.z())
			),
			new Vec3D(
				Math.min(max.x(), other.max.x()),
				Math.min(max.y(), other.max.y()),
				Math.min(max.z(), other.max.z())
			)
		);
	}
	
	public static AABBD intersect(AABBD a, AABBD b) {
		if(a != EMPTY && b != EMPTY) {
			return a.intersect(b);
		}
		return EMPTY;
	}
	
	public static AABBD intersect(AABBD ... aabbs) {
		AABBD aabb = INFINITE;
		for (AABBD a : aabbs) {
			aabb = intersect(aabb, a);
		}
		return aabb;
	}
	
	public AABBD transform(Matrix4X4 matrix) {
		if(this == INFINITE) {
			return this;
		}
		if(this == EMPTY) {
			return this;
		}
		
		Vec3D[] vertices = new Vec3D[]{
			new Vec3D(min.x(), max.y(), min.z()),
			new Vec3D(max.x(), max.y(), min.z()),
			new Vec3D(max.x(), max.y(), max.z()),
			new Vec3D(min.x(), max.y(), max.z()),
			new Vec3D(min.x(), min.y(), min.z()),
			new Vec3D(max.x(), min.y(), min.z()),
			new Vec3D(max.x(), min.y(), max.z()),
			new Vec3D(min.x(), min.y(), max.z())
		};
		
		AABBD aabb = EMPTY;
		for (Vec3D vec3D : vertices) {
			Vec3D vertex = matrix.transform(vec3D);
			aabb = AABBD.union(aabb, new AABBD(vertex, vertex));
		}
		
		return aabb;
	}
	
	public boolean badSize() {
		return min.x() > max.x() || min.y() > max.y() || min.z() > max.z();
	}
	
	public static AABBD badFilter(AABBD in) {
		return in.badSize() ? EMPTY : in;
	}
	
	public AABBD resolve() {
		Vec3D rMin = min.resolve();
		Vec3D rMax = max.resolve();
		if(rMin == Vec3D.NEG_INFINITY && rMax == Vec3D.INFINITY) {
			return INFINITE;
		}
		if(rMin == Vec3D.NAN || rMax == Vec3D.NAN) {
			return EMPTY;
		}
		return badFilter(this);
	}
	
}
