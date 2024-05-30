package com.ferreusveritas.node.shape;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.node.ports.*;
import com.ferreusveritas.node.NodeRegistryData;

import java.util.UUID;

/**
 * A Shape that slices another Shape along Y = 0 and infinitely extrudes the result in the Y direction.
 */
public class SliceShape extends Shape {
	
	public static final NodeRegistryData REGISTRY_DATA = new NodeRegistryData.Builder()
		.majorType(SHAPE)
		.minorType("slice")
		.loaderClass(Loader.class)
		.sceneObjectClass(SliceShape.class)
		.port(new PortDescription(PortDirection.OUT, PortDataTypes.SHAPE))
		.build();
	
	private final Shape shape;
	private final AABBD bounds;
	
	private SliceShape(UUID uuid, Shape shape) {
		super(uuid);
		this.shape = shape;
		this.bounds = calculateBounds(shape);
	}
	
	@Override
	public NodeRegistryData getRegistryData() {
		return REGISTRY_DATA;
	}
	
	private AABBD calculateBounds(Shape shape) {
		AABBD aabb = shape.bounds();
		return new AABBD(
			aabb.min().withY(Double.NEGATIVE_INFINITY),
			aabb.max().withY(Double.POSITIVE_INFINITY)
		);
	}
	
	@Override
	public AABBD bounds() {
		return bounds;
	}
	
	@Override
	public double getVal(Vec3D pos) {
		return shape.getVal(pos.withY(0));
	}
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends ShapeLoaderNode {
		
		private final InputPort<Shape> shapeInput;
		
		@JsonCreator
		public Loader(
			@JsonProperty(UID) UUID uuid,
			@JsonProperty(SHAPE) PortAddress shapeAddress
		) {
			super(uuid);
			this.shapeInput = createInputAndRegisterConnection(SHAPE, PortDataTypes.SHAPE, shapeAddress);
		}
		
		protected Shape create() {
			return new SliceShape(
				getUuid(),
				get(shapeInput)
			);
		}
		
	}
	
}
