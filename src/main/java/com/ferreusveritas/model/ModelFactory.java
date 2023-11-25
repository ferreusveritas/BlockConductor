package com.ferreusveritas.model;

import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.BiFactory;
import com.ferreusveritas.support.json.JsonObj;

public class ModelFactory {
	
	private static final BiFactory<Model> FACTORY = new BiFactory.Builder<Model>()
		.add(MeshModel.TYPE, MeshModel::new)
		.add(ReferenceModel.TYPE, ReferenceModel::new)
		.build();
	
	public static Model create(Scene scene, JsonObj src) {
		return FACTORY.create(scene, src);
	}
	
	private ModelFactory() {
		throw new IllegalStateException("Utility class");
	}

}
