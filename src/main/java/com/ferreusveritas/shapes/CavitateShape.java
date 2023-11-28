package com.ferreusveritas.shapes;

import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

import java.util.Optional;

/**
 * Hollows out a provided shape using a 3d kernel
 */
public class CavitateShape extends Shape {
	
	public static final String TYPE = "cavitate";
	
	private final Shape shape;
	
	public CavitateShape(Scene scene, Shape shape) {
		super(scene);
		this.shape = shape;
	}
	
	public CavitateShape(Scene scene, JsonObj src) {
		super(scene, src);
		this.shape = src.getObj(SHAPE).map(scene::createShape).orElseThrow(missing(SHAPE));
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public Optional<AABBI> getAABB() {
		return shape.getAABB();
	}
	
	@Override
	public boolean isInside(Vec3I pos) {
		return shape.isInside(pos) &&
			!(
				shape.isInside(pos.add(Vec3I.DOWN))
				&& shape.isInside(pos.add(Vec3I.UP))
				&& shape.isInside(pos.add(Vec3I.NORTH))
				&& shape.isInside(pos.add(Vec3I.SOUTH))
				&& shape.isInside(pos.add(Vec3I.EAST))
				&& shape.isInside(pos.add(Vec3I.WEST))
			);
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(SHAPE, shape);
	}
	
}
