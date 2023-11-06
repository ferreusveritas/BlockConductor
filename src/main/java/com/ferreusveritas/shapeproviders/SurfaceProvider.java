package com.ferreusveritas.shapeproviders;

import com.ferreusveritas.api.AABB;
import com.ferreusveritas.api.VecI;

public class SurfaceProvider implements ShapeProvider {
	
	private final AABB aabb;
	
	public SurfaceProvider(VecI dir, int offset) {
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
	public AABB getAABB() {
		return aabb;
	}
	
	@Override
	public boolean isInside(VecI pos) {
		return aabb.isPointInside(pos);
	}
	
}
