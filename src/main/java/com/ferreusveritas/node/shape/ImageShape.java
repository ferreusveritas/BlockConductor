package com.ferreusveritas.node.shape;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferreusveritas.resources.Resources;
import com.ferreusveritas.resources.image.ImageLoader;
import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.misc.MiscHelper;
import com.ferreusveritas.node.NodeRegistryData;
import com.ferreusveritas.node.ports.PortDataTypes;
import com.ferreusveritas.node.ports.PortDescription;
import com.ferreusveritas.node.ports.PortDirection;
import com.ferreusveritas.node.shape.support.ColorChannel;
import com.ferreusveritas.node.values.EnumNodeValue;
import com.ferreusveritas.node.values.StringNodeValue;

import java.awt.image.BufferedImage;
import java.util.UUID;

/**
 * BufferImage is a type of Image that loads a BufferedImage and uses the specified color channel as the value.
 */
public class ImageShape extends Shape {
	
	public static final String RESOURCE = "resource";
	public static final String CHANNEL = "channel";
	public static final NodeRegistryData REGISTRY_DATA = new NodeRegistryData.Builder()
		.majorType(SHAPE)
		.minorType("image")
		.loaderClass(Loader.class)
		.sceneObjectClass(ImageShape.class)
		.value(new StringNodeValue.Builder(RESOURCE).build())
		.value(new EnumNodeValue.Builder(CHANNEL).values(ColorChannel.class).def(ColorChannel.G).build())
		.port(new PortDescription(PortDirection.OUT, PortDataTypes.SHAPE))
		.build();
	
	private final float[] data;
	private final int dataWidth;
	private final AABBD bounds;
	
	private ImageShape(UUID uuid, String resource, ColorChannel channel) {
		super(uuid);
		BufferedImage image = Resources.load(resource, BufferedImage.class);
		this.data = createData(image, channel);
		this.dataWidth = image.getWidth();
		this.bounds = calculateBounds(image);
	}
	
	@Override
	public NodeRegistryData getRegistryData() {
		return REGISTRY_DATA;
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
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends ShapeLoaderNode {
		
		private final String resource;
		private final ColorChannel channel;
		
		@JsonCreator
		public Loader(
			@JsonProperty(UID) UUID uuid,
			@JsonProperty(RESOURCE) String resource,
			@JsonProperty(CHANNEL) ColorChannel channel
		) {
			super(uuid);
			this.resource = resource;
			this.channel = channel;
			validate();
		}
		
		private void validate() {
			MiscHelper.require(resource, RESOURCE);
			MiscHelper.require(channel, CHANNEL);
		}
		
		protected Shape create() {
			return new ImageShape(
				getUuid(),
				resource,
				channel
			);
		}
		
	}
	
}
