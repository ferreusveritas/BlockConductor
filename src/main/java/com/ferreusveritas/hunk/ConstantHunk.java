package com.ferreusveritas.hunk;

import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

/**
 * ConstantHunk is a Hunk that returns a constant value for all coordinates.
 */
public class ConstantHunk extends Hunk {
	
	public static final String TYPE = "constant";
	
	private final double value;
	
	public ConstantHunk(Scene scene, double value) {
		super(scene);
		this.value = value;
	}
	
	public ConstantHunk(Scene scene, JsonObj src) {
		super(scene, src);
		this.value = src.getDouble("value").orElse(0.0);
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public AABBI bounds() {
		return AABBI.INFINITE;
	}
	
	@Override
	public double getVal(Vec3D pos) {
		return value;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set("value", value);
	}
	
}
