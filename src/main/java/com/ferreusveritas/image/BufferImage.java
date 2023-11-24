package com.ferreusveritas.image;

import com.ferreusveritas.math.Pixel;
import com.ferreusveritas.math.RectI;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.InvalidJsonProperty;
import com.ferreusveritas.support.json.JsonObj;

import java.awt.image.BufferedImage;

public class BufferImage extends Image {
	
	public static final String TYPE = "buffer";
	
	private final String resource;
	private final BufferedImage image;
	private final RectI bounds;
	
	public BufferImage(Scene scene, String resource) {
		super(scene);
		this.resource = resource;
		this.image = ImageLoader.load(resource);
		this.bounds = new RectI(0, 0, image.getWidth(), image.getHeight());
	}
	
	public BufferImage(Scene scene, JsonObj src) {
		super(scene, src);
		this.resource = src.getString("resource").orElseThrow(() -> new InvalidJsonProperty("Missing resource"));
		this.image = ImageLoader.load(resource);
		this.bounds = new RectI(0, 0, image.getWidth(), image.getHeight());
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public RectI bounds() {
		return bounds;
	}
	
	@Override
	public Pixel getPixel(int x, int y) {
		if(bounds().isInside(x, y)) {
			return new Pixel(image.getRGB(x, y));
		}
		return Pixel.BLACK;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set("resource", resource);
	}
	
}
