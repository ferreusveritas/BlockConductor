package com.ferreusveritas.image;

import com.ferreusveritas.math.Pixel;
import com.ferreusveritas.math.RectI;

public abstract class Image {

	private final RectI bounds;
	
	protected Image(int width, int height) {
		this.bounds = new RectI(width, height);
	}

	public int width() {
		return bounds.width();
	}
	
	public int height() {
		return bounds.height();
	}
	
	public RectI bounds() {
		return bounds;
	}
	
	public abstract Pixel getPixel(int x, int y);
	
}
