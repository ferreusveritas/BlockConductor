package com.ferreusveritas.shape;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

public class ClipShape extends Shape {
	
	public static final String TYPE = "clip";
	
	private final Shape shape;
	private final AABBD bounds;
	
	public ClipShape(Scene scene, Shape shape, AABBD bounds) {
		super(scene);
		this.shape = shape;
		this.bounds = bounds;
	}
	
	public ClipShape(Scene scene, JsonObj src) {
		super(scene, src);
		this.shape = src.getObj(SHAPE).map(scene::createShape).orElseThrow(missing(SHAPE));
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
		if(bounds.contains(pos)) {
			return shape.getVal(pos);
		}
		return 0;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(BOUNDS, bounds)
			.set(SHAPE, shape);
	}
}
