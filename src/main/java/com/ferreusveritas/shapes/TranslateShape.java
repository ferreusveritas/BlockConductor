package com.ferreusveritas.shapes;

import com.ferreusveritas.api.AABB;
import com.ferreusveritas.api.VecI;

import java.util.Optional;

public class TranslateShape implements Shape {
	
	private final Shape shape;
	private final VecI offset;
	
	public TranslateShape(Shape shape, VecI offset) {
		this.shape = shape;
		this.offset = offset;
	}
	
	@Override
	public Optional<AABB> getAABB() {
		return shape.getAABB().map(aabb -> aabb.offset(offset));
	}
	
	@Override
	public boolean isInside(VecI pos) {
		return shape.isInside(pos.sub(offset));
	}
	
}
