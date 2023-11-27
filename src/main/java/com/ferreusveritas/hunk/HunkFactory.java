package com.ferreusveritas.hunk;

import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.BiFactory;
import com.ferreusveritas.support.json.JsonObj;
import com.ferreusveritas.transform.Transform;

public class HunkFactory {
	
	private static final BiFactory<Hunk> FACTORY = new BiFactory.Builder<Hunk>()
		.add(AddHunk.TYPE, AddHunk::new)
		.add(BlendHunk.TYPE, BlendHunk::new)
		.add(ClipHunk.TYPE, ClipHunk::new)
		.add(ConstantHunk.TYPE, ConstantHunk::new)
		.add(CurveHunk.TYPE, CurveHunk::new)
		.add(ImageHunk.TYPE, ImageHunk::new)
		.add(InvertHunk.TYPE, InvertHunk::new)
		.add(MaxHunk.TYPE, MaxHunk::new)
		.add(MinHunk.TYPE, MinHunk::new)
		.add(MulHunk.TYPE, MulHunk::new)
		.add(PerlinHunk.TYPE, PerlinHunk::new)
		.add(ReferenceHunk.TYPE, ReferenceHunk::new)
		.add(SimplexHunk.TYPE, SimplexHunk::new)
		.add(SubHunk.TYPE, SubHunk::new)
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
