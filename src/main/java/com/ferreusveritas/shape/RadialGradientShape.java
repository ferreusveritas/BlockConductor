package com.ferreusveritas.shape;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

/**
 * A Shape that represents a radial gradient from the origin
 */
public class RadialGradientShape extends Shape {

	public static final String TYPE = "radialGradient";
	public static final double DEFAULT_RADIUS = 1.0;
	
	private final double radius;
	private final AABBD bounds;
	
	public RadialGradientShape(Scene scene) {
		this(scene, DEFAULT_RADIUS);
	}
	
	public RadialGradientShape(Scene scene, double radius) {
		super(scene);
		this.radius = radius;
		this.bounds = calculateBounds(radius);
	}
	
	public RadialGradientShape(Scene scene, JsonObj src) {
		super(scene, src);
		this.radius = src.getDouble("radius").orElse(DEFAULT_RADIUS);
		this.bounds = calculateBounds(radius);
	}
	
	private AABBD calculateBounds(double radius) {
		if(radius <= 0) {
			throw new IllegalArgumentException("radius must be greater than zero");
		}
		return new AABBD(
			new Vec3D(-radius, -radius, -radius),
			new Vec3D(radius, radius, radius)
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
		double dist = pos.lenSq() / (radius * radius);
		if(dist > 1.0) {
			return 0.0;
		}
		return 1.0 - dist;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set("radius", radius);
	}
	
}
