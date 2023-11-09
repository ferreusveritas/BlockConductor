package com.ferreusveritas.shapes;

import com.ferreusveritas.api.AABB;
import com.ferreusveritas.api.VecI;

import java.util.Optional;

public class UbiqueShape implements Shape {

	public UbiqueShape(Shape shape) {}
	
	@Override
	public Optional<AABB> getAABB() {
		return Optional.of(AABB.INFINITE);
	}
	
	@Override
	public boolean isInside(VecI pos) {
		return true;
	}
	
}
