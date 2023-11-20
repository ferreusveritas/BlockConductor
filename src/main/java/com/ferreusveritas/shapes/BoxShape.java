package com.ferreusveritas.shapes;

import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.support.json.InvalidJsonProperty;
import com.ferreusveritas.support.json.JsonObj;

import java.util.Optional;

public class BoxShape extends Shape {
	
	public static final String TYPE = "box";
	
	private final AABBI aabb;
	
	public BoxShape(AABBI aabb) {
		super();
		this.aabb = aabb;
	}
	
	public BoxShape(JsonObj src) {
		super(src);
		this.aabb = new AABBI(src.getObj("aabb").orElseThrow(() -> new InvalidJsonProperty("Missing aabb")));
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
		return aabb.isPointInside(pos);
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set("aabb", aabb);
	}
	
}
