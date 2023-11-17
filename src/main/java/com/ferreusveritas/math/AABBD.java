package com.ferreusveritas.math;

import com.ferreusveritas.model.SimpleFace;
import com.ferreusveritas.model.SimpleMeshModel;

import java.util.List;

public record AABBD(
	Vec3D min,
	Vec3D max
) {
	
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
	
	public AABBI toAABBI() {
		return new AABBI(this);
	}
	
	public RectD toRect() {
		return new RectD(this);
	}
	
	public AABBD offset(Vec3D offset) {
		return new AABBD(min.add(offset), max.add(offset));
	}
	
	public AABBD expand(double amount) {
		return new AABBD(min.sub(new Vec3D(amount, amount, amount)), max.add(new Vec3D(amount, amount, amount)));
	}

	public AABBD expand(Vec3D amount) {
		return new AABBD(min.sub(amount), max.add(amount));
	}

	public AABBD shrink(double amount) {
		return expand(-amount);
	}

	public AABBD shrink(Vec3D amount) {
		return expand(amount.neg());
	}
	
	public boolean isInside(Vec3D pos) {
		return
			pos.x() >= min.x() && pos.x() <= max.x() &&
			pos.y() >= min.y() && pos.y() <= max.y() &&
			pos.z() >= min.z() && pos.z() <= max.z();
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
	
	public SimpleMeshModel toMeshModel() {
		Vec3D p1 = new Vec3D(min.x(), max.y(), min.z());
		Vec3D p2 = new Vec3D(max.x(), max.y(), min.z());
		Vec3D p3 = new Vec3D(max.x(), max.y(), max.z());
		Vec3D p4 = new Vec3D(min.x(), max.y(), max.z());
		Vec3D p5 = new Vec3D(min.x(), min.y(), min.z());
		Vec3D p6 = new Vec3D(max.x(), min.y(), min.z());
		Vec3D p7 = new Vec3D(max.x(), min.y(), max.z());
		Vec3D p8 = new Vec3D(min.x(), min.y(), max.z());
		
		SimpleFace t1 = new SimpleFace(new Vec3D[]{ p1, p2, p3 });
		SimpleFace t2 = new SimpleFace(new Vec3D[]{ p1, p3, p4 });
		SimpleFace b1 = new SimpleFace(new Vec3D[]{ p5, p7, p6 });
		SimpleFace b2 = new SimpleFace(new Vec3D[]{ p5, p8, p7 });
		
		return new SimpleMeshModel(List.of(t1, t2, b1, b2));
	}
	
}
