package com.ferreusveritas.transform;

import com.ferreusveritas.math.Matrix4X4;
import com.ferreusveritas.support.json.JsonObj;

import java.util.List;

public class Transforms extends Transform {
	
	public static final String TYPE = "transforms";
	
	private final List<Transform> operations;
	private final Matrix4X4 matrix;
	
	public Transforms(List<Transform> operations) {
		this.operations = List.copyOf(operations);
		this.matrix = createMatrix(operations);
	}
	
	public Transforms(Transform ... transforms) {
		this(List.of(transforms));
	}
	
	public Transforms(JsonObj src) {
		this(src.getObj("operations").orElseThrow().toImmutableList(TransformFactory::create));
	}
	
	private Matrix4X4 createMatrix(List<Transform> transforms) {
		return transforms.stream().map(Transform::getMatrix).reduce(Matrix4X4.IDENTITY, Matrix4X4::mul);
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
			.set("transforms", operations);
	}
}
