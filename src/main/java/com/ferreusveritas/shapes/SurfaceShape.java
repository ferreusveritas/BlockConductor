package com.ferreusveritas.shapes;

import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

import java.util.Optional;

public class SurfaceShape extends Shape {
	
	public static final String TYPE = "surface";
	public static final String DIR = "dir";
	public static final String OFFSET = "offset";
	
	private final Vec3I dir;
	private final int offset;
	private final AABBI aabb;
	
	public SurfaceShape(Scene scene, Vec3I dir, int offset) {
		super(scene);
		this.dir = dir;
		this.offset = offset;
		this.aabb = createAABB(dir.mul(offset), dir);
	}
	
	public SurfaceShape(Scene scene, JsonObj src) {
		super(scene, src);
		this.dir = src.getObj(DIR).map(Vec3I::new).orElse(Vec3I.UP);
		this.offset = src.getInt(OFFSET).orElse(0);
		this.aabb = createAABB(dir.mul(offset), dir);
	}
	
	@Override
	public String getType() {
		return TYPE;
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
		return aabb.isInside(pos);
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(DIR, dir)
			.set(OFFSET, offset);
	}
	
}
