package com.ferreusveritas.shapes;

import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.support.json.InvalidJsonProperty;
import com.ferreusveritas.support.json.JsonObj;

import java.util.Optional;

public class SphereShape extends Shape {
	
	public static final String TYPE = "sphere";
	
	private final Vec3D center;
	private final double radius;
	private final AABBI aabb;
	
	public SphereShape(Vec3D center, double radius) {
		this.center = center;
		this.radius = radius;
		Vec3I v = center.toVecI();
		this.aabb = new AABBI(v, v).expand((int)Math.ceil(radius)).orElseThrow();
	}
	
	public SphereShape(JsonObj src) {
		super(src);
		this.center = src.getObj("center").map(Vec3D::new).orElseThrow(() -> new InvalidJsonProperty("Missing center property"));
		this.radius = src.getDouble("radius").orElse(0.0);
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
			.set("center", center)
			.set("radius", radius);
	}
	
}
