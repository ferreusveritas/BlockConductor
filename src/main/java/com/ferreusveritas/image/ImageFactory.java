package com.ferreusveritas.image;

import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.BiFactory;
import com.ferreusveritas.support.json.JsonObj;

public class ImageFactory {
	
	private static final BiFactory<Image> FACTORY = new BiFactory.Builder<Image>()
		.add(BufferImage.TYPE, BufferImage::new)
		.build();
	
	public static Image create(Scene scene, JsonObj src) {
		return FACTORY.create(scene, src);
	}
	
	private ImageFactory() {
		throw new IllegalStateException("Utility class");
	}
	
}
