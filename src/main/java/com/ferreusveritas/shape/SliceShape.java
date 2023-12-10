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
	private final AABBD bounds;
	
	public SliceShape(Scene scene, Shape shape) {
		super(scene);
		this.shape = shape;
		this.bounds = calculateBounds(shape);
	}
	
	public SliceShape(Scene scene, JsonObj src) {
		super(scene, src);
		this.shape = src.getObj(SHAPE).map(scene::createShape).orElseThrow(missing(SHAPE));
		this.bounds = calculateBounds(shape);
	}
	
	private AABBD calculateBounds(Shape shape) {
		AABBD aabb = shape.bounds();
		return new AABBD(
			aabb.min().withY(Double.NEGATIVE_INFINITY),
			aabb.max().withY(Double.POSITIVE_INFINITY)
		);
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public AABBD bounds() {
		return bounds;
	}
	
	@Override
	public double getVal(Vec3D pos) {
		return shape.getVal(pos.withY(0));
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(SHAPE, shape);
	}
	
}
