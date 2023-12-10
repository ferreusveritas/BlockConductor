package com.ferreusveritas.math;

import com.ferreusveritas.support.json.InvalidJsonProperty;
import com.ferreusveritas.support.json.JsonObj;

public record AABBD(
	Vec3D min, // inclusive
	Vec3D max // exclusive
) {
	
	public static AABBD INFINITE = new AABBD(
		new Vec3D(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY),
		new Vec3D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY)
	);
	
	public static AABBD EMPTY = new AABBD(Vec3I.ZERO, Vec3I.ZERO);
	
	public AABBD(AABBI aabb) {
		this(aabb.min().toVecD(), aabb.max().toVecD());
	}
	
	public AABBD(Vec3I min, Vec3I max) {
		this(min.toVecD().floor(), max.toVecD().ceil());
	}
	
	public AABBD(RectD rect) {
		this(
			new Vec3D(rect.x1(), Double.NEGATIVE_INFINITY, rect.z1()),
			new Vec3D(rect.x2(), Double.POSITIVE_INFINITY, rect.z2())
		);
	}
	
	public AABBD(JsonObj src) {
		this(
			src.getObj("min").map(Vec3D::new).orElseThrow(() -> new InvalidJsonProperty("Missing min")),
			src.getObj("max").map(Vec3D::new).orElseThrow(() -> new InvalidJsonProperty("Missing max"))
		);
	}
	
	public AABBI toAABBI() {
		if(this == INFINITE) {
			return AABBI.INFINITE;
		}
		return new AABBI(this);
	}
	
	public RectD toRect() {
		return new RectD(this);
	}
	
	public AABBD offset(Vec3D offset) {
		if(this == INFINITE) {
			return this;
		}
		return new AABBD(min.add(offset), max.add(offset));
	}
	
	public AABBD expand(double amount) {
		if(this == INFINITE) {
			return this;
		}
		return new AABBD(min.sub(new Vec3D(amount, amount, amount)), max.add(new Vec3D(amount, amount, amount)));
	}

	public AABBD expand(Vec3D amount) {
		if(this == INFINITE) {
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
		return
			pos.x() >= min.x() && pos.x() < max.x() &&
			pos.y() >= min.y() && pos.y() < max.y() &&
			pos.z() >= min.z() && pos.z() < max.z();
	}
	
	public boolean intersects(AABBD other) {
		return
			min.x() < other.max.x() && max.x() > other.min.x() &&
			min.y() < other.max.y() && max.y() > other.min.y() &&
			min.z() < other.max.z() && max.z() > other.min.z();
	}
	
	public static AABBD union(AABBD a, AABBD b) {
		if(a != null && b != null) {
			return a.union(b);
		}
		return a != null ? a : b;
	}
	
	public AABBD union(AABBD other) {
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
	
	public AABBD intersect(AABBD other) {
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
		if(a != null && b != null) {
			return a.intersect(b);
		}
		return null;
	}
	
	public AABBD transform(Matrix4X4 matrix) {
		if(this == INFINITE) {
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
		
		AABBD aabb = null;
		for (Vec3D vec3D : vertices) {
			Vec3D vertex = matrix.transform(vec3D);
			aabb = AABBD.union(aabb, new AABBD(vertex, vertex));
		}
		
		return aabb;
	}
	
}
