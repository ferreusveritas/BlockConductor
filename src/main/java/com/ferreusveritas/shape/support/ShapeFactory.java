package com.ferreusveritas.shape.support;

import com.ferreusveritas.shape.*;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.BiFactory;
import com.ferreusveritas.support.json.JsonObj;

public class ShapeFactory {
	
	private static final BiFactory<Shape> FACTORY = new BiFactory.Builder<Shape>()
		.add(BlendShape.TYPE, BlendShape::new)
		.add(BoxShape.TYPE, BoxShape::new)
		.add(CacheShape.TYPE, CacheShape::new)
		.add(CavitateShape.TYPE, CavitateShape::new)
		.add(CheckerShape.TYPE, CheckerShape::new)
		.add(ClipShape.TYPE, ClipShape::new)
		.add(CombineShape.TYPE, CombineShape::new)
		.add(ConstantShape.TYPE, ConstantShape::new)
		.add(CubeShape.TYPE, CubeShape::new)
		.add(CurveShape.TYPE, CurveShape::new)
		.add(CylinderShape.TYPE, CylinderShape::new)
		.add(GradientShape.TYPE, GradientShape::new)
		.add(HeightMapShape.TYPE, HeightMapShape::new)
		.add(ImageShape.TYPE, ImageShape::new)
		.add(LayerShape.TYPE, LayerShape::new)
		.add(ModelShape.TYPE, ModelShape::new)
		.add(PerlinShape.TYPE, PerlinShape::new)
		.add(PlaneShape.TYPE, PlaneShape::new)
		.add(PyramidShape.TYPE, PyramidShape::new)
		.add(RadialGradientShape.TYPE, RadialGradientShape::new)
		.add(ReferenceShape.TYPE, ReferenceShape::new)
		.add(SimplexShape.TYPE, SimplexShape::new)
		.add(SliceShape.TYPE, SliceShape::new)
		.add(SphereShape.TYPE, SphereShape::new)
		.add(TransformShape.TYPE, TransformShape::new)
		.add(TranslateShape.TYPE, TranslateShape::new)
		.add(VoidShape.TYPE, VoidShape::new)
		.build();
	
	public static Shape create(Scene scene, JsonObj src) {
		return FACTORY.create(scene, src);
	}
	
	private ShapeFactory() {
		throw new IllegalStateException("Utility class");
	}
	
}
