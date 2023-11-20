package com.ferreusveritas.image;

import com.ferreusveritas.support.Factory;
import com.ferreusveritas.support.json.JsonObj;

public class ImageFactory {
	
	private static final Factory<Image> factory = new Factory.Builder<Image>()
		.add(BufferImage.TYPE, BufferImage::new)
		.build();
	
	public static Image create(JsonObj src) {
		return factory.create(src);
	}
	
	private ImageFactory() {
		throw new IllegalStateException("Utility class");
	}
	
}
