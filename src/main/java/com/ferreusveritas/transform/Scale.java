package com.ferreusveritas.transform;

import com.ferreusveritas.math.Matrix4X4;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.support.json.JsonObj;

public class Scale extends Transform {

	public static final String TYPE = "scale";
	
	private final double factor;
	private final Matrix4X4 matrix;
	
	public Scale(double factor) {
		this.factor = factor;
		this.matrix = Matrix4X4.IDENTITY.scale(new Vec3D(factor, factor, factor));
	}
	
	public Scale(JsonObj src) {
		this(src.getDouble("factor").orElse(1.0));
	}
	
	@Override
	public Matrix4X4 getMatrix() {
		return matrix;
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set("factor", factor);
	}
	
}
