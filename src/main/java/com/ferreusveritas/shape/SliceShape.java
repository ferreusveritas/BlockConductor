package com.ferreusveritas.shape;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

/**
 * A Shape that slices another Shape along Y = 0 and infinitely extrudes the result in the Y direction.
 */
public class SliceShape extends Shape {

	public static final String TYPE = "slice";
	
	private final Shape shape;
	private final AABBD aabb;
	
	public SliceShape(Scene scene, Shape shape) {
		super(scene);
		this.shape = shape;
		this.aabb = calcBounds(shape);
	}
	
	public SliceShape(Scene scene, JsonObj src) {
		super(scene, src);
		this.shape = src.getObj("shape").map(scene::createShape).orElseThrow(missing("shape"));
		this.aabb = calcBounds(shape);
	}
	
	private AABBD calcBounds(Shape shape) {
		AABBD bounds = shape.bounds();
		return new AABBD(
			bounds.min().withY(Double.NEGATIVE_INFINITY),
			bounds.max().withY(Double.POSITIVE_INFINITY)
		);
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public AABBD bounds() {
		return aabb;
	}
	
	@Override
	public double getVal(Vec3D pos) {
		return shape.getVal(pos.withY(0));
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set("shape", shape);
	}
	
}
