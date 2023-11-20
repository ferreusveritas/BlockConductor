package com.ferreusveritas.transform;

import com.ferreusveritas.math.Matrix4X4;
import com.ferreusveritas.support.Factory;
import com.ferreusveritas.support.json.JsonObj;

public class TransformFactory {

	private static final Factory<Transform> FACTORY = new Factory.Builder<Transform>()
		.add(MatrixTransform.TYPE, MatrixTransform::new)
		.build();
	
	public static Transform create(JsonObj src) {
		return FACTORY.create(src);
	}
	
	private TransformFactory() {
		throw new IllegalStateException("Utility class");
	}
	
}
