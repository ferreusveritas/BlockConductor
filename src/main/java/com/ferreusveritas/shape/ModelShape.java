package com.ferreusveritas.shape;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.model.Model;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

/**
 * A Shape that uses a Model
 */
public class ModelShape extends Shape {
	
	public static final String TYPE = "model";
	public static final String MODEL = "model";
	
	private final Model model;
	private final AABBD bounds;
	
	public ModelShape(Scene scene, Model model) {
		super(scene);
		this.model = model;
		this.bounds = model.getAABB();
	}
	
	public ModelShape(Scene scene, JsonObj src) {
		super(scene, src);
		this.model = src.getObj(MODEL).map(scene::createModel).orElseThrow(missing(MODEL));
		this.bounds = model.getAABB();
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
		return bounds.contains(pos) && model.pointIsInside(pos) ? 1.0 : 0.0;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(MODEL, model);
	}
	
}
