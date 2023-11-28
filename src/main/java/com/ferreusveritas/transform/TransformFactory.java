package com.ferreusveritas.transform;

import com.ferreusveritas.support.Factory;
import com.ferreusveritas.support.json.JsonObj;

public class TransformFactory {

	private static final Factory<Transform> FACTORY = new Factory.Builder<Transform>()
		.add(MatrixTransform.TYPE, MatrixTransform::new)
		.add(RotateX.TYPE, RotateX::new)
		.add(RotateY.TYPE, RotateY::new)
		.add(RotateZ.TYPE, RotateZ::new)
		.add(Scale.TYPE, Scale::new)
		.add(Transforms.TYPE, Transforms::new)
		.add(Translate.TYPE, Translate::new)
		.build();
	
	public static Transform create(JsonObj src) {
		return FACTORY.create(src);
	}
	
	private TransformFactory() {
		throw new IllegalStateException("Utility class");
	}
	
}
