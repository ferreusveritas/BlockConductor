package com.ferreusveritas.image;

import com.ferreusveritas.math.RectI;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

/**
 * ConstantImage is an Image that returns a constant value for all coordinates.
 */
public class ConstantImage extends Image {
	
	public static final String TYPE = "constant";
	
	private final double value;
	
	public ConstantImage(Scene scene, double value) {
		super(scene);
		this.value = value;
	}
	
	public ConstantImage(Scene scene, JsonObj src) {
		super(scene, src);
		this.value = src.getDouble("value").orElse(0.0);
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public RectI bounds() {
		return RectI.INFINITE;
	}
	
	@Override
	public double getVal(int x, int y) {
		return value;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set("value", value);
	}
	
}
