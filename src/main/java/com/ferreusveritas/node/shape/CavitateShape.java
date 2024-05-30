package com.ferreusveritas.node.shape;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.node.ports.*;
import com.ferreusveritas.node.NodeRegistryData;

import java.util.UUID;

public class CavitateShape extends Shape {
	
	public static final NodeRegistryData REGISTRY_DATA = new NodeRegistryData.Builder()
		.majorType(SHAPE)
		.minorType("cavitate")
		.loaderClass(Loader.class)
		.sceneObjectClass(CavitateShape.class)
		.port(new PortDescription(PortDirection.IN, PortDataTypes.SHAPE))
		.port(new PortDescription(PortDirection.OUT, PortDataTypes.SHAPE))
		.build();
	
	private final Shape shape;
	private final AABBD bounds;
	
	private CavitateShape(UUID uuid, Shape shape) {
		super(uuid);
		this.shape = shape;
		this.bounds = calculateBounds();
	}
	
	@Override
	public NodeRegistryData getRegistryData() {
		return REGISTRY_DATA;
	}
	
	private AABBD calculateBounds() {
		return shape.bounds();
	}
	
	@Override
	public AABBD bounds() {
		return bounds;
	}
	
	@Override
	public double getVal(Vec3D pos) {
		return bounds.contains(pos) && inside(pos) ? 1.0 : 0.0;
	}
	
	private boolean inside(Vec3D pos) {
		return inShape(pos) &&
			!(
				inShape(pos.down()) &&
					inShape(pos.up()) &&
					inShape(pos.north()) &&
					inShape(pos.south()) &&
					inShape(pos.west()) &&
					inShape(pos.east())
			);
	}
	
	private boolean inShape(Vec3D pos) {
		return shape.getVal(pos) >= 0.5;
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
			return new CavitateShape(
				getUuid(),
				get(shapeInput)
			);
		}
		
	}
	
}
