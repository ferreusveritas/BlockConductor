package com.ferreusveritas.shapes;

import com.ferreusveritas.math.AABB;
import com.ferreusveritas.math.VecI;

import java.util.Optional;

public class CheckerShape implements Shape {
	
	@Override
	public Optional<AABB> getAABB() {
		return Optional.of(AABB.INFINITE);
	}
	
	@Override
	public boolean isInside(VecI pos) {
		return (pos.x() + pos.y() + pos.z()) % 2 == 0;
	}
	
}
