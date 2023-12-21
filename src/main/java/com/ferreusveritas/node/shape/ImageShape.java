package com.ferreusveritas.node.shape;

import com.ferreusveritas.image.ImageLoader;
import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.node.NodeLoader;
import com.ferreusveritas.node.shape.support.ColorChannel;
import com.ferreusveritas.scene.LoaderSystem;
import com.ferreusveritas.support.json.JsonObj;

import java.awt.image.BufferedImage;
import java.util.UUID;

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
	
	private ImageShape(UUID uuid, String resource, ColorChannel channel) {
		super(uuid);
		this.resource = resource;
		this.channel = channel;
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
	
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder {
		
		private UUID uuid = null;
		private String resource = null;
		private ColorChannel channel = ColorChannel.G;
		
		public Builder uuid(UUID uuid) {
			this.uuid = uuid;
			return this;
		}
		
		public Builder resource(String resource) {
			this.resource = resource;
			return this;
		}
		
		public Builder channel(ColorChannel channel) {
			this.channel = channel;
			return this;
		}
		
		public ImageShape build() {
			if(resource == null) {
				throw new IllegalStateException("resource cannot be null");
			}
			return new ImageShape(uuid, resource, channel);
		}
		
	}
	
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends NodeLoader {
		
		private final String resource;
		private final ColorChannel channel;
		
		public Loader(LoaderSystem loaderSystem, JsonObj src) {
			super(loaderSystem, src);
			this.resource = src.getString(RESOURCE).orElseThrow(missing(RESOURCE));
			this.channel = src.getString(CHANNEL).flatMap(ColorChannel::of).orElse(ColorChannel.G);
		}
		
		@Override
		public Shape load(LoaderSystem loaderSystem) {
			return new ImageShape(getUuid(), resource, channel);
		}
		
	}
	
}
