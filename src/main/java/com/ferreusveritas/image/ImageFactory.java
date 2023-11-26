package com.ferreusveritas.image;

import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.BiFactory;
import com.ferreusveritas.support.json.JsonObj;

public class ImageFactory {
	
	private static final BiFactory<Image> FACTORY = new BiFactory.Builder<Image>()
		.add(BufferImage.TYPE, BufferImage::new)
		.add(ReferenceImage.TYPE, ReferenceImage::new)
		.add(PerlinImage.TYPE, PerlinImage::new)
		.add(SimplexImage.TYPE, SimplexImage::new)
		.add(AddImage.TYPE, AddImage::new)
		.add(SubImage.TYPE, SubImage::new)
		.add(MulImage.TYPE, MulImage::new)
		.add(ClipImage.TYPE, ClipImage::new)
		.add(ConstantImage.TYPE, ConstantImage::new)
		.add(BlendImage.TYPE, BlendImage::new)
		.add(MinImage.TYPE, MinImage::new)
		.add(MaxImage.TYPE, MaxImage::new)
		.add(ThresholdImage.TYPE, ThresholdImage::new)
		.add(InvertImage.TYPE, InvertImage::new)
		.add(CurveImage.TYPE, CurveImage::new)
		.build();
	
	public static Image create(Scene scene, JsonObj src) {
		return FACTORY.create(scene, src);
	}
	
	private ImageFactory() {
		throw new IllegalStateException("Utility class");
	}
	
}
