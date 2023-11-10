package com.ferreusveritas.shapes;

import com.ferreusveritas.math.AABB;
import com.ferreusveritas.math.VecI;

import java.util.Optional;

public class SurfaceShape implements Shape {
	
	private final AABB aabb;
	
	public SurfaceShape(VecI dir, int offset) {
		this.aabb = createAABB(dir.mul(offset), dir);
	}
	
	private AABB createAABB(VecI pos, VecI dir) {
		if (dir.equals(VecI.UP)) {
			return new AABB(new VecI(Integer.MIN_VALUE, pos.y(), Integer.MIN_VALUE), new VecI(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE));
		}
		if (dir.equals(VecI.DOWN)) {
			return new AABB(new VecI(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE), new VecI(Integer.MAX_VALUE, pos.y(), Integer.MAX_VALUE));
		}
		if (dir.equals(VecI.NORTH)) {
			return new AABB(new VecI(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE), new VecI(Integer.MAX_VALUE, Integer.MAX_VALUE, pos.z()));
		}
		if (dir.equals(VecI.SOUTH)) {
			return new AABB(new VecI(Integer.MIN_VALUE, Integer.MIN_VALUE, pos.z()), new VecI(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE));
		}
		if (dir.equals(VecI.EAST)) {
			return new AABB(new VecI(pos.x(), Integer.MIN_VALUE, Integer.MIN_VALUE), new VecI(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE));
		}
		if (dir.equals(VecI.WEST)) {
			return new AABB(new VecI(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE), new VecI(pos.x(), Integer.MAX_VALUE, Integer.MAX_VALUE));
		}
		return null;
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
