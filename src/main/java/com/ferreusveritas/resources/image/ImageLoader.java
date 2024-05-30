package com.ferreusveritas.resources.image;

import com.ferreusveritas.resources.ResourceLoaderException;
import com.ferreusveritas.resources.ResourceLoader;
import com.ferreusveritas.support.storage.Storage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class ImageLoader implements ResourceLoader<BufferedImage> {
	
	public BufferedImage load(InputStream stream) {
		try {
			return ImageIO.read(stream);
		} catch (Exception e) {
			throw new ResourceLoaderException("Failed to load image from stream", e);
		}
	}
	
}
