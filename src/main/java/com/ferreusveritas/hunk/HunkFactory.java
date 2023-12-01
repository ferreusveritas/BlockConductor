package com.ferreusveritas.hunk;

import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.BiFactory;
import com.ferreusveritas.support.json.JsonObj;

public class HunkFactory {
	
	private static final BiFactory<Hunk> FACTORY = new BiFactory.Builder<Hunk>()
		.add(AddHunk.TYPE, AddHunk::new)
		.add(BlendHunk.TYPE, BlendHunk::new)
		.add(ClipHunk.TYPE, ClipHunk::new)
		.add(ConstantHunk.TYPE, ConstantHunk::new)
		.add(CubeHunk.TYPE, CubeHunk::new)
		.add(CurveHunk.TYPE, CurveHunk::new)
		.add(CylinderHunk.TYPE, CylinderHunk::new)
		.add(GradientHunk.TYPE, GradientHunk::new)
		.add(HeightMapHunk.TYPE, HeightMapHunk::new)
		.add(ImageHunk.TYPE, ImageHunk::new)
		.add(InvertHunk.TYPE, InvertHunk::new)
		.add(MaxHunk.TYPE, MaxHunk::new)
		.add(MinHunk.TYPE, MinHunk::new)
		.add(ModelHunk.TYPE, ModelHunk::new)
		.add(MultiplyHunk.TYPE, MultiplyHunk::new)
		.add(PlaneHunk.TYPE, PlaneHunk::new)
		.add(PerlinHunk.TYPE, PerlinHunk::new)
		.add(PyramidHunk.TYPE, PyramidHunk::new)
		.add(RadialGradientHunk.TYPE, RadialGradientHunk::new)
		.add(ReferenceHunk.TYPE, ReferenceHunk::new)
		.add(SimplexHunk.TYPE, SimplexHunk::new)
		.add(SliceHunk.TYPE, SliceHunk::new)
		.add(SubtractHunk.TYPE, SubtractHunk::new)
		.add(ThresholdHunk.TYPE, ThresholdHunk::new)
		.add(TransformHunk.TYPE, TransformHunk::new)
		.build();
	
	public static Hunk create(Scene scene, JsonObj src) {
		return FACTORY.create(scene, src);
	}
	
	private HunkFactory() {
		throw new IllegalStateException("Utility class");
	}
	
}
