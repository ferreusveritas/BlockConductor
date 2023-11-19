package com.ferreusveritas.image;

import com.ferreusveritas.support.storage.Storage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class ImageLoader {
	
	public static BufferImage load(String path) {
		try {
			InputStream stream = Storage.getInputStream(path);
			return load(stream);
		} catch (Exception e) {
			throw new ImageLoaderException("Failed to load image from path: " + path, e);
		}
	}
	
	private static BufferImage load(InputStream stream) throws IOException {
		BufferedImage image = ImageIO.read(stream);
		return new BufferImage(image);
	}
	
	private ImageLoader() {
		throw new IllegalStateException("Utility class");
	}
	
}
