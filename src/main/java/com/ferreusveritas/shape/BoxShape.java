package com.ferreusveritas.shape;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

public class BoxShape extends Shape {
	
	public static final String TYPE = "box";
	
	private final AABBD bounds;
	
	public BoxShape(Scene scene, AABBD bounds) {
		super(scene);
		this.bounds = bounds;
	}
	
	public BoxShape(Scene scene, JsonObj src) {
		super(scene, src);
		this.bounds = src.getObj(BOUNDS).map(AABBD::new).orElseThrow(missing(BOUNDS));
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
		return bounds.contains(pos) ? 1.0 : 0.0;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(BOUNDS, bounds);
	}
	
}
