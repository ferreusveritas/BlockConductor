package com.ferreusveritas.shapes;

import com.ferreusveritas.api.AABB;
import com.ferreusveritas.api.VecI;

import java.util.Optional;

public class VoidShape implements Shape {
	
	@Override
	public Optional<AABB> getAABB() {
		return Optional.empty();
	}
	
	@Override
	public boolean isInside(VecI pos) {
		return false;
	}
	
}
