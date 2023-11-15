package com.ferreusveritas.shapes;

import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Axis;
import com.ferreusveritas.math.Vec3I;

import java.util.Optional;

public class CheckerShape implements Shape {
	
	private final Axis axis; // null means all axes(3D checker block)
	
	public CheckerShape() {
		this(null);
	}
	
	public CheckerShape(Axis axis) {
		this.axis = axis;
	}
	
	@Override
	public Optional<AABBI> getAABB() {
		return Optional.of(AABBI.INFINITE);
	}
	
	@Override
	public boolean isInside(Vec3I pos) {
		if(axis == null) {
			return (pos.x() + pos.y() + pos.z()) % 2 == 0;
		}
		return switch (axis) {
			case X -> pos.y() + pos.z() % 2 == 0;
			case Y -> pos.x() + pos.z() % 2 == 0;
			case Z -> pos.x() + pos.y() % 2 == 0;
		};
		
	}
	
}
