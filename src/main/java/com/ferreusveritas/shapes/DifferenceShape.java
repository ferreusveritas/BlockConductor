package com.ferreusveritas.shapes;

import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.support.json.InvalidJsonProperty;
import com.ferreusveritas.support.json.JsonObj;

import java.util.Optional;

/**
 * Remove the presence of the second shape from the first shape with a Difference (-) operation
 */
public class DifferenceShape extends Shape {
	
	public static final String TYPE = "difference";
	
	private final Shape shape1;
	private final Shape shape2;
	
	public DifferenceShape(Shape shape1, Shape shape2) {
		this.shape1 = shape1;
		this.shape2 = shape2;
	}
	
	public DifferenceShape(JsonObj src) {
		super(src);
		this.shape1 = src.getObj("shape1").map(ShapeFactory::create).orElseThrow(() -> new InvalidJsonProperty("Missing shape1 property"));
		this.shape2 = src.getObj("shape2").map(ShapeFactory::create).orElseThrow(() -> new InvalidJsonProperty("Missing shape2 property"));
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public Optional<AABBI> getAABB() {
		return shape1.getAABB();
	}
	
	@Override
	public boolean isInside(Vec3I pos) {
		return shape1.isInside(pos) && !shape2.isInside(pos);
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set("shape1", shape1)
			.set("shape2", shape2);
	}
	
}
