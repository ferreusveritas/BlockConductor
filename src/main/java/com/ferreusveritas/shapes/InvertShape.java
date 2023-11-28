package com.ferreusveritas.shapes;

import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

import java.util.Optional;

/**
 * Inverts the represented shape, so it's empty where there's blocks and vice versa.
 */
public class InvertShape extends Shape {
	
	public static final String TYPE = "invert";
	
	private final Shape shape;
	
	public InvertShape(Scene scene, Shape shape) {
		super(scene);
		this.shape = shape;
	}
	
	public InvertShape(Scene scene, JsonObj src) {
		super(scene, src);
		this.shape = src.getObj(SHAPE).map(scene::createShape).orElseThrow(missing(SHAPE));
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public Optional<AABBI> getAABB() {
		return Optional.of(AABBI.INFINITE);
	}
	
	@Override
	public boolean isInside(Vec3I pos) {
		return !shape.isInside(pos);
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(SHAPE, shape);
	}
	
}
