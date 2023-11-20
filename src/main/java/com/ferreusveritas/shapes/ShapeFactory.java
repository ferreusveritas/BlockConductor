package com.ferreusveritas.shapes;

import com.ferreusveritas.support.Factory;
import com.ferreusveritas.support.json.JsonObj;

public class ShapeFactory {
	
	private static final Factory<Shape> factory = new Factory.Builder<Shape>()
		.add(BoxShape.TYPE, BoxShape::new)
		.add(CacheShape.TYPE, CacheShape::new)
		.add(CavitateShape.TYPE, CavitateShape::new)
		.add(CheckerShape.TYPE, CheckerShape::new)
		.add(DifferenceShape.TYPE, DifferenceShape::new)
		.add(HeightmapShape.TYPE, HeightmapShape::new)
		.add(IntersectShape.TYPE, IntersectShape::new)
		.add(InvertShape.TYPE, InvertShape::new)
		.add(LayerShape.TYPE, LayerShape::new)
		.add(MeshModelShape.TYPE, MeshModelShape::new)
		.add(SphereShape.TYPE, SphereShape::new)
		.add(SurfaceShape.TYPE, SurfaceShape::new)
		.add(TranslateShape.TYPE, TranslateShape::new)
		.add(UbiquitousShape.TYPE, UbiquitousShape::new)
		.add(UnionShape.TYPE, UnionShape::new)
		.add(VoidShape.TYPE, VoidShape::new)
		.build();
		
	public static Shape create(JsonObj src) {
		return factory.create(src);
	}
	
	private ShapeFactory() {
		throw new IllegalStateException("Utility class");
	}
	
}
