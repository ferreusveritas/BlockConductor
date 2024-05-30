package com.ferreusveritas.node.shape;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.node.ports.*;
import com.ferreusveritas.node.NodeRegistryData;
import com.ferreusveritas.node.values.VecNodeValue;

import java.util.UUID;

public class ClipShape extends Shape {
	
	public static final String MIN = "min";
	public static final String MAX = "max";
	public static final NodeRegistryData REGISTRY_DATA = new NodeRegistryData.Builder()
		.majorType(SHAPE)
		.minorType("clip")
		.loaderClass(Loader.class)
		.sceneObjectClass(ClipShape.class)
		.value(new VecNodeValue.Builder(MIN).build())
		.value(new VecNodeValue.Builder(MAX).build())
		.port(new PortDescription(PortDirection.IN, PortDataTypes.SHAPE))
		.port(new PortDescription(PortDirection.OUT, PortDataTypes.SHAPE))
		.build();
	
	private final Shape shape;
	private final AABBD bounds;
	
	public ClipShape(UUID uuid, Shape shape, AABBD bounds) {
		super(uuid);
		this.shape = shape;
		this.bounds = bounds;
	}
	
	@Override
	public NodeRegistryData getRegistryData() {
		return REGISTRY_DATA;
	}
	
	@Override
	public AABBD bounds() {
		return bounds;
	}
	
	@Override
	public double getVal(Vec3D pos) {
		if(bounds.contains(pos)) {
			return shape.getVal(pos);
		}
		return 0.0;
	}

	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends ShapeLoaderNode {
		
		private final AABBD bounds;
		private final InputPort<Shape> shapeInput;
		
		@JsonCreator
		public Loader(
			@JsonProperty(UID) UUID uuid,
			@JsonProperty(MIN) Vec3D min,
			@JsonProperty(MAX) Vec3D max,
			@JsonProperty(SHAPE) PortAddress shapeAddress
		) {
			super(uuid);
			this.bounds = new AABBD(min, max);
			this.shapeInput = createInputAndRegisterConnection(PortDataTypes.SHAPE, shapeAddress);
		}
		
		protected Shape create() {
			return new ClipShape(
				getUuid(),
				get(shapeInput),
				bounds
			);
		}
		
	}
	
}
