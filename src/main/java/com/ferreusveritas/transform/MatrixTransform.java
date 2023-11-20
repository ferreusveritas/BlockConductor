package com.ferreusveritas.transform;

import com.ferreusveritas.math.Matrix4X4;
import com.ferreusveritas.support.json.JsonObj;

public class MatrixTransform extends Transform {
	
	public static final String TYPE = "matrix";
	
	private final Matrix4X4 matrix;
	
	public MatrixTransform() {
		super();
		this.matrix = Matrix4X4.IDENTITY;
	}
	
	public MatrixTransform(Matrix4X4 matrix) {
		super();
		this.matrix = matrix;
	}
	
	public MatrixTransform(JsonObj src) {
		super();
		this.matrix = src.getObj("data").map(Matrix4X4::new).orElse(Matrix4X4.IDENTITY);
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
			.set("data", matrix);
	}
	
}
