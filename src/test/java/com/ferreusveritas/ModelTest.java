package com.ferreusveritas;

import com.ferreusveritas.resources.model.obj.ObjModelPart;
import com.ferreusveritas.resources.model.obj.ObjModelLoader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ModelTest {
	
	@Test
	void test() {
		ObjModelPart model = ObjModelLoader.load("res://models/dragon_skull.obj").getPart("dragon_skull").orElseThrow();
		assertEquals(1852, model.getFaceCount());
	}
	
}
