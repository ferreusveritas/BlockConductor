package com.ferreusveritas.node.shape;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.node.ports.*;
import com.ferreusveritas.node.NodeRegistryData;

import java.util.UUID;

public class GrowShape extends Shape {
	
	public static final NodeRegistryData REGISTRY_DATA = new NodeRegistryData.Builder()
		.majorType(SHAPE)
		.minorType("grow")
		.loaderClass(Loader.class)
		.sceneObjectClass(GrowShape.class)
		.port(new PortDescription(PortDirection.OUT, PortDataTypes.SHAPE))
		.build();
	
	private final Shape shape;
	private final AABBD bounds;
	
	private GrowShape(UUID uuid, Shape shape) {
		super(uuid);
		this.shape = shape;
		this.bounds = calculateBounds();
	}
	
	@Override
	public NodeRegistryData getRegistryData() {
		return REGISTRY_DATA;
	}
	
	private AABBD calculateBounds() {
		return shape.bounds().expand(1.0);
	}
	
	@Override
	public AABBD bounds() {
		return bounds;
	}
	
	@Override
	public double getVal(Vec3D pos) {
		if(!bounds.contains(pos)){
			return 0.0;
		}
		return
			isInside(pos.down()) ||
			isInside(pos.up()) ||
			isInside(pos.north()) ||
			isInside(pos.south()) ||
			isInside(pos.west()) ||
			isInside(pos.east())
			? 1.0 : 0.0;
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
			this.shapeInput = createInputAndRegisterConnection(PortDataTypes.SHAPE, shapeAddress);
		}
		
		protected Shape create() {
			return new GrowShape(
				getUuid(),
				get(shapeInput)
			);
		}
		
	}
	
}
