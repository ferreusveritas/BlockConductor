package com.ferreusveritas.image;

import com.ferreusveritas.math.Pixel;

import java.awt.image.BufferedImage;

public class BufferImage extends Image {
	
	public final BufferedImage image;
	
	public BufferImage(BufferedImage image) {
		super(image.getWidth(), image.getHeight());
		this.image = image;
	}
	
	@Override
	public Pixel getPixel(int x, int y) {
		if(bounds().isInside(x, y)) {
			return new Pixel(image.getRGB(x, y));
		}
		return Pixel.BLACK;
	}
}
