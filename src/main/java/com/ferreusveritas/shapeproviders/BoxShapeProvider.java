package com.ferreusveritas.shapeproviders;

import com.ferreusveritas.api.AABB;
import com.ferreusveritas.api.VecI;

public class BoxShapeProvider implements ShapeProvider {
	
	private final AABB aabb;
	
	public BoxShapeProvider(AABB aabb) {
		this.aabb = aabb;
	}
	
	@Override
	public AABB getAABB() {
		return aabb;
	}
	
	@Override
	public boolean isInside(VecI pos) {
		return aabb.isPointInside(pos);
	}
	
}
