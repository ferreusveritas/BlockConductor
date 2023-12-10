package com.ferreusveritas.shape;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

public class LayerShape extends Shape {
	
	public static final String TYPE = "layer";
	public static final String MIN = "min";
	public static final String MAX = "max";
	
	private final double min; // inclusive
	private final double max; // exclusive
	private final AABBD bounds;
	
	public LayerShape(Scene scene, double min, double max) {
		super(scene);
		this.min = min;
		this.max = max;
		this.bounds = calculateBounds();
	}
	
	public LayerShape(Scene scene, JsonObj src) {
		super(scene, src);
		this.min = src.getDouble(MIN).orElse(0.0);
		this.max = src.getDouble(MAX).orElse(1.0);
		this.bounds = calculateBounds();
	}
	
	private AABBD calculateBounds() {
		return new AABBD(
			new Vec3D(Double.NEGATIVE_INFINITY, min, Double.NEGATIVE_INFINITY),
			new Vec3D(Double.POSITIVE_INFINITY, max, Double.POSITIVE_INFINITY)
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
		return pos.y() >= min && pos.y() < max ? 1.0 : 0.0;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(MIN, min)
			.set(MAX, max);
	}
	
}
