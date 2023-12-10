package com.ferreusveritas.shape;

import com.ferreusveritas.math.*;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

/**
 * BlendShape is a type of Shape that blends the values of two shapes together using a blend shape as a mask.
 */
public class BlendShape extends Shape {
	
	public static final String TYPE = "blend";
	public static final String SHAPE_1 = "shape1";
	public static final String SHAPE_2 = "shape2";
	public static final String BLEND = "blend";
	
	private final Shape shape1;
	private final Shape shape2;
	private final Shape blend;
	private final AABBD bounds;
	
	public BlendShape(Scene scene, Shape shape1, Shape shape2, Shape blend) {
		super(scene);
		this.shape1 = shape1;
		this.shape2 = shape2;
		this.blend = blend;
		this.bounds = calculateBounds();
	}
	
	public BlendShape(Scene scene, JsonObj src) {
		super(scene, src);
		this.shape1 = src.getObj(SHAPE_1).map(scene::createShape).orElseThrow(missing(SHAPE_1));
		this.shape2 = src.getObj(SHAPE_2).map(scene::createShape).orElseThrow(missing(SHAPE_2));
		this.blend = src.getObj(BLEND).map(scene::createShape).orElseThrow(missing(BLEND));
		this.bounds = calculateBounds();
	}
	
	private AABBD calculateBounds() {
		return AABBD.union(shape1.bounds(), shape2.bounds());
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
		if(!bounds.contains(pos)){
			return 0.0;
		}
		double blendVal = blend.getVal(pos);
		if(blendVal < 0.0){
			return shape1.getVal(pos);
		}
		if(blendVal > 1.0){
			return shape2.getVal(pos);
		}
		double val1 = shape1.getVal(pos);
		double val2 = shape2.getVal(pos);
		return MathHelper.lerp(blendVal, val1, val2);
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(SHAPE_1, shape1)
			.set(SHAPE_2, shape2)
			.set(BLEND, blend);
	}
}
