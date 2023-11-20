package com.ferreusveritas.shapes;

import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.support.json.InvalidJsonProperty;
import com.ferreusveritas.support.json.JsonObj;

import java.util.Optional;

/**
 * Inverts the represented shape, so it's empty where there's blocks and vice versa.
 */
public class InvertShape extends Shape {
	
	public static final String TYPE = "invert";
	
	private final Shape shape;
	
	public InvertShape(Shape shape) {
		this.shape = shape;
	}
	
	public InvertShape(JsonObj src) {
		super(src);
		this.shape = src.getObj("shape").map(ShapeFactory::create).orElseThrow(() -> new InvalidJsonProperty("Missing shape"));
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
			.set("shape", shape);
	}
	
}
