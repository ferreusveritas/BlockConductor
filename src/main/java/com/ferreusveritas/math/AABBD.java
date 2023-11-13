package com.ferreusveritas.math;

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
	
}