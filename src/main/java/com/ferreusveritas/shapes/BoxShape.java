package com.ferreusveritas.shapes;

import com.ferreusveritas.math.AABB;
import com.ferreusveritas.math.VecI;

import java.util.Optional;

public class BoxShape implements Shape {
	
	private final AABB aabb;
	
	public BoxShape(AABB aabb) {
		this.aabb = aabb;
	}
	
	@Override
	public Optional<AABB> getAABB() {
		return Optional.of(aabb);
	}
	
	@Override
	public boolean isInside(VecI pos) {
		return aabb.isPointInside(pos);
	}
	
}
