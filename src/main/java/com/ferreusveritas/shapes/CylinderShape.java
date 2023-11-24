package com.ferreusveritas.shapes;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.InvalidJsonProperty;
import com.ferreusveritas.support.json.JsonObj;

import java.util.Optional;

/**
 * A vertically oriented cylinder shape
 */
public class CylinderShape extends Shape {
	
	public static final String TYPE = "cylinder";
	
	private final Vec3D center;
	private final double radius;
	private final int height;
	
	public CylinderShape(Scene scene, Vec3D center, double radius, int h) {
		super(scene);
		this.center = center;
		this.radius = radius;
		this.height = h;
	}
	
	public CylinderShape(Scene scene, JsonObj src) {
		super(scene, src);
		this.center = src.getObj("center").map(Vec3D::new).orElseThrow(() -> new InvalidJsonProperty("Missing center property"));
		this.radius = src.getDouble("radius").orElse(0.0);
		this.height = src.getInt("height").orElse(0);
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public Optional<AABBI> getAABB() {
		AABBD aabb = new AABBD(center.add(new Vec3D(-radius, 0, -radius)), center.add(new Vec3D(radius, height, radius)));
		return Optional.of(aabb.toAABBI());
	}
	
	@Override
	public boolean isInside(Vec3I pos) {
		Vec3D delta = pos.toVecD().sub(center);
		return delta.y() >= 0 && delta.y() < height && delta.x() * delta.x() + delta.z() * delta.z() <= radius * radius;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set("center", center)
			.set("radius", radius)
			.set("height", height);
	}
	
}
