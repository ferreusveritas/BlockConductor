package com.ferreusveritas.shapes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;

import java.util.Optional;

public class SurfaceShape implements Shape {
	
	private final AABBI aabb;
	
	@JsonCreator
	public SurfaceShape(
		@JsonProperty("dir") Vec3I dir,
		@JsonProperty("offset") int offset
	) {
		this.aabb = createAABB(dir.mul(offset), dir);
	}
	
	private AABBI createAABB(Vec3I pos, Vec3I dir) {
		if (dir.equals(Vec3I.UP)) {
			return new AABBI(new Vec3I(Integer.MIN_VALUE, pos.y(), Integer.MIN_VALUE), new Vec3I(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE));
		}
		if (dir.equals(Vec3I.DOWN)) {
			return new AABBI(new Vec3I(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE), new Vec3I(Integer.MAX_VALUE, pos.y(), Integer.MAX_VALUE));
		}
		if (dir.equals(Vec3I.NORTH)) {
			return new AABBI(new Vec3I(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE), new Vec3I(Integer.MAX_VALUE, Integer.MAX_VALUE, pos.z()));
		}
		if (dir.equals(Vec3I.SOUTH)) {
			return new AABBI(new Vec3I(Integer.MIN_VALUE, Integer.MIN_VALUE, pos.z()), new Vec3I(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE));
		}
		if (dir.equals(Vec3I.EAST)) {
			return new AABBI(new Vec3I(pos.x(), Integer.MIN_VALUE, Integer.MIN_VALUE), new Vec3I(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE));
		}
		if (dir.equals(Vec3I.WEST)) {
			return new AABBI(new Vec3I(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE), new Vec3I(pos.x(), Integer.MAX_VALUE, Integer.MAX_VALUE));
		}
		return null;
	}
	
	@Override
	public Optional<AABBI> getAABB() {
		return Optional.of(aabb);
	}
	
	@Override
	public boolean isInside(Vec3I pos) {
		return aabb.isPointInside(pos);
	}
	
}
