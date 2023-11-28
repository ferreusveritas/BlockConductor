package com.ferreusveritas.shapes;

import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

import java.util.Optional;

public class SphereShape extends Shape {
	
	public static final String TYPE = "sphere";
	public static final String CENTER = "center";
	public static final String RADIUS = "radius";
	
	private final Vec3D center;
	private final double radius;
	private final AABBI aabb;
	
	public SphereShape(Scene scene, Vec3D center, double radius) {
		super(scene);
		this.center = center;
		this.radius = radius;
		Vec3I v = center.toVecI();
		this.aabb = new AABBI(v, v).expand((int)Math.ceil(radius)).orElseThrow();
	}
	
	public SphereShape(Scene scene, JsonObj src) {
		super(scene, src);
		this.center = src.getObj(CENTER).map(Vec3D::new).orElseThrow(missing(CENTER));
		this.radius = src.getDouble(RADIUS).orElse(0.0);
		Vec3I v = center.toVecI();
		this.aabb = new AABBI(v, v).expand((int)Math.ceil(radius)).orElseThrow();
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public Optional<AABBI> getAABB() {
		return Optional.of(aabb);
	}
	
	@Override
	public boolean isInside(Vec3I pos) {
		return center.distanceSqTo(pos.toVecD()) <= radius * radius;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(CENTER, center)
			.set(RADIUS, radius);
	}
	
}
