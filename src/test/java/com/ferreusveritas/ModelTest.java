package com.ferreusveritas;

import com.ferreusveritas.model.FullMeshModel;
import com.ferreusveritas.model.ModelLoader;
import com.ferreusveritas.model.SimpleFace;
import com.ferreusveritas.model.SimpleMeshModel;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ModelTest {
	
	@Test
	void test() {
		FullMeshModel model = ModelLoader.loadResource("/dragon_skull.obj").orElseThrow();
		assertEquals(1852, model.getFaceCount());
		SimpleMeshModel simpleMeshModel = model.toSimpleMeshModel();
		assertEquals(1852, simpleMeshModel.getFaceCount());
	}
	
}
