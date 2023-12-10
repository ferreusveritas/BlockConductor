package com.ferreusveritas.shape;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

public class CylinderShape extends Shape {

	public static final String TYPE = "cylinder";
	
	public static final double DEFAULT_HEIGHT = 1;
	public static final double DEFAULT_RADIUS = 1.0;
	
	private final double height;
	private final double radius;
	private final AABBD bounds;
	
	public CylinderShape(Scene scene, double height, double radius) {
		super(scene);
		this.height = height;
		this.radius = radius;
		this.bounds = calculateBounds();
	}
	
	public CylinderShape(Scene scene, JsonObj src) {
		super(scene, src);
		this.height = src.getDouble("height").orElse(DEFAULT_HEIGHT);
		this.radius = src.getDouble("radius").orElse(DEFAULT_RADIUS);
		this.bounds = calculateBounds();
	}
	
	private AABBD calculateBounds() {
		return new AABBD(
			new Vec3D(-radius, 0, -radius),
			new Vec3D(radius, height, radius)
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
		return pos.y() >= 0 && pos.y() < height && pos.withY(0.0).lenSq() < radius * radius ? 1.0 : 0.0;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set("height", height)
			.set("radius", radius);
	}
	
}
