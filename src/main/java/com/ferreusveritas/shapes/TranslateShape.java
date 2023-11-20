package com.ferreusveritas.shapes;

import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.support.json.InvalidJsonProperty;
import com.ferreusveritas.support.json.JsonObj;

import java.util.Optional;

/**
 * Offset a shape by a specified amount.
 */
public class TranslateShape extends Shape {
	
	public static final String TYPE = "translate";
	
	private final Shape shape;
	private final Vec3I offset;
	
	public TranslateShape(Shape shape, Vec3I offset) {
		this.shape = shape;
		this.offset = offset;
	}
	
	public TranslateShape(JsonObj src) {
		super(src);
		this.shape = src.getObj("shape").map(ShapeFactory::create).orElseThrow(() -> new InvalidJsonProperty("Missing shape"));
		this.offset = src.getObj("offset").map(Vec3I::new).orElseThrow(() -> new InvalidJsonProperty("Missing offset"));
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
			.set("shape", shape)
			.set("offset", offset);
	}
	
}
