package com.ferreusveritas.shapes;

import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

import java.util.Optional;

public class BoxShape extends Shape {
	
	public static final String TYPE = "box";
	public static final String AABB = "aabb";
	
	private final AABBI aabb;
	
	public BoxShape(Scene scene, AABBI aabb) {
		super(scene);
		this.aabb = aabb;
	}
	
	public BoxShape(Scene scene, JsonObj src) {
		super(scene, src);
		this.aabb = new AABBI(src.getObj(AABB).orElseThrow(missing(AABB)));
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
		return aabb.isInside(pos);
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(AABB, aabb);
	}
	
}
