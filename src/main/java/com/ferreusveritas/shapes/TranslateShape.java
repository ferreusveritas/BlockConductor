package com.ferreusveritas.shapes;

import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

import java.util.Optional;

/**
 * Offset a shape by a specified amount.
 */
public class TranslateShape extends Shape {
	
	public static final String TYPE = "translate";
	public static final String SHAPE = "shape";
	public static final String OFFSET = "offset";
	
	private final Shape shape;
	private final Vec3I offset;
	
	public TranslateShape(Scene scene, Shape shape, Vec3I offset) {
		super(scene);
		this.shape = shape;
		this.offset = offset;
	}
	
	public TranslateShape(Scene scene, JsonObj src) {
		super(scene, src);
		this.shape = src.getObj(SHAPE).map(scene::createShape).orElseThrow(missing(SHAPE));
		this.offset = src.getObj(OFFSET).map(Vec3I::new).orElseThrow(missing(OFFSET));
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public Optional<AABBI> getAABB() {
		return shape.getAABB().map(aabb -> aabb.offset(offset));
	}
	
	@Override
	public boolean isInside(Vec3I pos) {
		return shape.isInside(pos.sub(offset));
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(OFFSET, offset)
			.set(SHAPE, shape);
	}
	
}
