package com.ferreusveritas.image;

import com.ferreusveritas.math.RectI;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.InvalidJsonProperty;
import com.ferreusveritas.support.json.JsonObj;

import java.awt.image.BufferedImage;

/**
 * BufferImage is a type of Image that loads a BufferedImage and uses the specified color channel as the value.
 */
public class BufferImage extends Image {
	
	public static final String TYPE = "buffer";
	
	private final String resource;
	private final ColorChannel channel;
	private final float[] data;
	private final int dataWidth;
	private final RectI bounds;
	
	public BufferImage(Scene scene, String resource) {
		this(scene, resource, ColorChannel.G);
	}
	
	public BufferImage(Scene scene, String resource, ColorChannel channel) {
		super(scene);
		this.resource = resource;
		this.channel = channel;
		BufferedImage image = ImageLoader.load(resource);
		this.data = createData(image, channel);
		this.dataWidth = image.getWidth();
		this.bounds = new RectI(0, 0, image.getWidth(), image.getHeight());
	}
	
	public BufferImage(Scene scene, JsonObj src) {
		super(scene, src);
		this.resource = src.getString("resource").orElseThrow(() -> new InvalidJsonProperty("Missing resource"));
		this.channel = src.getString("channel").flatMap(ColorChannel::of).orElse(ColorChannel.G);
		BufferedImage image = ImageLoader.load(resource);
		this.data = createData(image, channel);
		this.dataWidth = image.getWidth();
		this.bounds = new RectI(0, 0, image.getWidth(), image.getHeight());
	}
	
	private float[] createData(BufferedImage image, ColorChannel channel) {
		int width = image.getWidth();
		int height = image.getHeight();
		float[] buffer = new float[width * height];
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				int rgb = image.getRGB(x, y);
				int val = channel.getColor(rgb);
				buffer[y * width + x] = val / 255.0f;
			}
		}
		return buffer;
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
	public double getVal(int x, int y) {
		if(bounds().isInside(x, y)) {
			return data[y * dataWidth + x];
		}
		return 0.0;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set("resource", resource)
			.set("channel", channel);
	}
	
}
