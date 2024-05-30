package com.ferreusveritas.node.shape;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.node.ports.*;
import com.ferreusveritas.node.NodeRegistryData;
import com.ferreusveritas.node.values.NumberNodeValue;

import java.util.UUID;

public class HeightMapShape extends Shape {
	
	public static final String HEIGHT = "height";
	public static final String MIN_Y = "minY";
	public static final double DEFAULT_HEIGHT = 255.0;
	public static final double DEFAULT_MIN_Y = 0.0;
	public static final NodeRegistryData REGISTRY_DATA = new NodeRegistryData.Builder()
		.majorType(SHAPE)
		.minorType("heightMap")
		.loaderClass(Loader.class)
		.sceneObjectClass(HeightMapShape.class)
		.value(new NumberNodeValue.Builder(HEIGHT).def(DEFAULT_HEIGHT).build())
		.value(new NumberNodeValue.Builder(MIN_Y).def(DEFAULT_MIN_Y).build())
		.port(new PortDescription(PortDirection.OUT, PortDataTypes.SHAPE))
		.build();
	
	private final Shape shape;
	private final double height;
	private final double minY;
	private final AABBD bounds;
	
	private HeightMapShape(UUID uuid, Shape shape, double height, double minY) {
		super(uuid);
		this.shape = shape;
		this.height = height;
		this.minY = minY;
		this.bounds = calculateBounds(height);
		validate();
	}
	
	@Override
	public NodeRegistryData getRegistryData() {
		return REGISTRY_DATA;
	}
	
	private void validate() {
		if(height < 1.0) {
			throw new IllegalArgumentException("Height must be at least 1");
		}
	}
	
	public AABBD calculateBounds(double height) {
		AABBD shapeBounds = shape.bounds();
		Vec3D min = shapeBounds.min().withY(Double.NEGATIVE_INFINITY);
		Vec3D max = shapeBounds.max().withY(height);
		return new AABBD(min, max);
	}
	
	@Override
	public AABBD bounds() {
		return bounds;
	}
	
	@Override
	public double getVal(Vec3D pos) {
		if(!bounds.contains(pos)) {
			return 0.0;
		}
		if(pos.y() < minY) {
			return 1.0;
		}
		double val = shape.getVal(pos.withY(0.0));
		double h = val * height;
		if(pos.y() < h) {
			return 1.0;
		}
		return 0.0;
	}
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends ShapeLoaderNode {
		
		private final double height;
		private final double minY;
		private final InputPort<Shape> shapeInput;
		
		@JsonCreator
		public Loader(
			@JsonProperty(UID) UUID uuid,
			@JsonProperty(HEIGHT) Double height,
			@JsonProperty(MIN_Y) Double minY,
			@JsonProperty(SHAPE) PortAddress shapeAddress
		) {
			super(uuid);
			this.height = height;
			this.minY = minY;
			this.shapeInput = createInputAndRegisterConnection(PortDataTypes.SHAPE, shapeAddress);
		}

		protected Shape create() {
			return new HeightMapShape(
				getUuid(),
				get(shapeInput),
				height,
				minY
			);
		}
		
	}
	
}
