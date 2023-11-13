package com.ferreusveritas.image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class ImageLoader {
	
	public static BufferImage loadImageFromFilePath(String filePath) {
		File file = new File(filePath);
		return loadImageFromFile(file);
	}
	
	public static BufferImage loadImageFromFile(File file) {
		try {
			InputStream stream = new FileInputStream(file);
			return loadImage(stream);
		} catch (IOException e) {
			throw new ImageLoaderException("Failed to load image from file: " + file.getAbsoluteFile(), e);
		}
	}
	
	public static BufferImage loadImageFromResource(String path) {
		InputStream stream = ImageLoader.class.getResourceAsStream(path);
		try {
			return loadImage(stream);
		} catch (IOException e) {
			throw new ImageLoaderException("Failed to load image from resource: " + path, e);
		}
	}
	
	public static BufferImage loadImageFromStream(InputStream stream) {
		try {
			return loadImage(stream);
		} catch (IOException e) {
			throw new ImageLoaderException("Failed to load image from stream", e);
		}
	}
	
	private static BufferImage loadImage(InputStream stream) throws IOException {
		BufferedImage image = ImageIO.read(stream);
		return new BufferImage(image);
	}
	
	private ImageLoader() {
		throw new IllegalStateException("Utility class");
	}
	
}
