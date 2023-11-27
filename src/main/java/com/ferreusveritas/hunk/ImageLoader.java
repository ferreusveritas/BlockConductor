package com.ferreusveritas.hunk;

import com.ferreusveritas.support.storage.Storage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class ImageLoader {
	
	public static BufferedImage load(String path) {
		try {
			InputStream stream = Storage.getInputStream(path);
			return load(stream);
		} catch (Exception e) {
			throw new ImageLoaderException("Failed to load image from path: " + path, e);
		}
	}
	
	private static BufferedImage load(InputStream stream) throws IOException {
		return ImageIO.read(stream);
	}
	
	private ImageLoader() {
		throw new IllegalStateException("Utility class");
	}
	
}
