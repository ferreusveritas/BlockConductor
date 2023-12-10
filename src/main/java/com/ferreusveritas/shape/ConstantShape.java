package com.ferreusveritas.shape;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

/**
 * ConstantShape is a Shape that returns a constant value for all coordinates.
 */
public class ConstantShape extends Shape {
	
	public static final String TYPE = "constant";
	
	private final double value;
	
	public ConstantShape(Scene scene, double value) {
		super(scene);
		this.value = value;
	}
	
	public ConstantShape(Scene scene, JsonObj src) {
		super(scene, src);
		this.value = src.getDouble("value").orElse(0.0);
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public AABBD bounds() {
		return AABBD.INFINITE;
	}
	
	@Override
	public double getVal(Vec3D pos) {
		return value;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set("value", value);
	}
	
}
