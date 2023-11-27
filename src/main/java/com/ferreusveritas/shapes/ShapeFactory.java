package com.ferreusveritas.shapes;

import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.BiFactory;
import com.ferreusveritas.support.json.JsonObj;

public class ShapeFactory {
	
	private static final BiFactory<Shape> FACTORY = new BiFactory.Builder<Shape>()
		.add(BoxShape.TYPE, BoxShape::new)
		.add(CacheShape.TYPE, CacheShape::new)
		.add(CavitateShape.TYPE, CavitateShape::new)
		.add(CheckerShape.TYPE, CheckerShape::new)
		.add(CylinderShape.TYPE, CylinderShape::new)
		.add(DifferenceShape.TYPE, DifferenceShape::new)
		.add(HeightmapShape.TYPE, HeightmapShape::new)
		.add(HunkShape.TYPE, HunkShape::new)
		.add(IntersectShape.TYPE, IntersectShape::new)
		.add(InvertShape.TYPE, InvertShape::new)
		.add(LayerShape.TYPE, LayerShape::new)
		.add(ModelShape.TYPE, ModelShape::new)
		.add(ReferenceShape.TYPE, ReferenceShape::new)
		.add(SphereShape.TYPE, SphereShape::new)
		.add(SurfaceShape.TYPE, SurfaceShape::new)
		.add(TranslateShape.TYPE, TranslateShape::new)
		.add(UbiquitousShape.TYPE, UbiquitousShape::new)
		.add(UnionShape.TYPE, UnionShape::new)
		.add(VoidShape.TYPE, VoidShape::new)
		.build();
		
	public static Shape create(Scene scene, JsonObj src) {
		return FACTORY.create(scene, src);
	}
	
	private ShapeFactory() {
		throw new IllegalStateException("Utility class");
	}
	
}
