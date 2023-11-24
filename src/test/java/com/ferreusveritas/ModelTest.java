package com.ferreusveritas;

import com.ferreusveritas.model.ObjModel;
import com.ferreusveritas.model.ObjModelLoader;
import com.ferreusveritas.model.MeshModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ModelTest {
	
	@Test
	void test() {
		ObjModel model = ObjModelLoader.load("res://dragon_skull.obj").orElseThrow();
		assertEquals(1852, model.getFaceCount());
	}
	
}
