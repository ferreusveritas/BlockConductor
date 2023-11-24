package com.ferreusveritas.transform;

import com.ferreusveritas.math.Matrix4X4;
import com.ferreusveritas.support.json.JsonObj;

public class RotateX extends Transform {
	
	public static final String TYPE = "rotatex";
	
	private final double angle;
	private final Matrix4X4 matrix;
	
	public RotateX(double angle) {
		this.angle = angle;
		this.matrix = Matrix4X4.IDENTITY.rotateX(Math.toRadians(angle));
	}
	
	public RotateX(JsonObj src) {
		this(src.getDouble("angle").orElse(0.0));
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
			.set("angle", angle);
	}
	
}
