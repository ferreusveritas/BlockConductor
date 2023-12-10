package com.ferreusveritas.shape;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

public class SphereShape extends Shape {
	
	public static final String TYPE = "sphere";
	
	private final double radius;
	private final AABBD bounds;
	
	public SphereShape(Scene scene, double radius) {
		super(scene);
		this.radius = radius;
		this.bounds = calculateBounds(radius);
	}
	
	public SphereShape(Scene scene, JsonObj src) {
		super(scene, src);
		this.radius = src.getDouble("radius").orElse(1.0);
		this.bounds = calculateBounds(radius);
	}
	
	private static AABBD calculateBounds(double radius) {
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
		return pos.lenSq() < radius * radius ? 1.0 : 0.0;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set("radius", radius);
	}
	
}
