package com.ferreusveritas.shape;

import com.ferreusveritas.image.ImageLoader;
import com.ferreusveritas.math.*;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.shape.support.ColorChannel;
import com.ferreusveritas.support.json.JsonObj;

import java.awt.image.BufferedImage;

/**
 * BufferImage is a type of Image that loads a BufferedImage and uses the specified color channel as the value.
 */
public class ImageShape extends Shape {
	
	public static final String TYPE = "image";
	public static final String RESOURCE = "resource";
	public static final String CHANNEL = "channel";
	
	private final String resource;
	private final ColorChannel channel;
	private final float[] data;
	private final int dataWidth;
	private final AABBD bounds;
	
	public ImageShape(Scene scene, String resource) {
		this(scene, resource, ColorChannel.G);
	}
	
	public ImageShape(Scene scene, String resource, ColorChannel channel) {
		super(scene);
		this.resource = resource;
		this.channel = channel;
		BufferedImage image = ImageLoader.load(resource);
		this.data = createData(image, channel);
		this.dataWidth = image.getWidth();
		this.bounds = calculateBounds(image);
	}
	
	public ImageShape(Scene scene, JsonObj src) {
		super(scene, src);
		this.resource = src.getString(RESOURCE).orElseThrow(missing(RESOURCE));
		this.channel = src.getString(CHANNEL).flatMap(ColorChannel::of).orElse(ColorChannel.G);
		BufferedImage image = ImageLoader.load(resource);
		this.data = createData(image, channel);
		this.dataWidth = image.getWidth();
		this.bounds = calculateBounds(image);
	}
	
	private AABBD calculateBounds(BufferedImage image) {
		return new AABBD(
			new Vec3D(0.0, Double.NEGATIVE_INFINITY, 0.0),
			new Vec3D(image.getWidth(), Double.POSITIVE_INFINITY, image.getHeight())
		);
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
	public AABBD bounds() {
		return bounds;
	}
	
	@Override
	public double getVal(Vec3D pos) {
		if(bounds.contains(pos)) {
			Vec3I vec = pos.toVecI();
			return data[vec.z() * dataWidth + vec.x()];
		}
		return 0.0;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(RESOURCE, resource)
			.set(CHANNEL, channel);
	}
	
}
