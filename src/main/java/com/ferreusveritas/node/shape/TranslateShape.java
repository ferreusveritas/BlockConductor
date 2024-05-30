package com.ferreusveritas.node.shape;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.node.ports.*;
import com.ferreusveritas.node.NodeRegistryData;
import com.ferreusveritas.node.values.VecNodeValue;

import java.util.UUID;

public class TranslateShape extends Shape {
	
	public static final String OFFSET = "offset";
	public static final NodeRegistryData REGISTRY_DATA = new NodeRegistryData.Builder()
		.majorType(SHAPE)
		.minorType("translate")
		.loaderClass(Loader.class)
		.sceneObjectClass(TranslateShape.class)
		.value(new VecNodeValue.Builder(OFFSET).def(Vec3D.ZERO).build())
		.port(new PortDescription(PortDirection.IN, PortDataTypes.SHAPE))
		.port(new PortDescription(PortDirection.OUT, PortDataTypes.SHAPE))
		.build();
	
	private final Shape shape;
	private final Vec3D offset;
	private final AABBD bounds;
	
	private TranslateShape(UUID uuid, Shape shape, Vec3D offset) {
		super(uuid);
		this.shape = shape;
		this.offset = offset;
		this.bounds = calculateBounds(shape);
	}
	
	@Override
	public NodeRegistryData getRegistryData() {
		return REGISTRY_DATA;
	}
	
	private AABBD calculateBounds(Shape shape) {
		return shape.bounds().offset(offset);
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
		return shape.getVal(pos.sub(offset));
	}
	
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends ShapeLoaderNode {
		
		private final Vec3D offset;
		private final InputPort<Shape> shapeInput;
		
		@JsonCreator
		public Loader(
			@JsonProperty(UID) UUID uuid,
			@JsonProperty(OFFSET) Vec3D offset,
			@JsonProperty(SHAPE) PortAddress shape
		) {
			super(uuid);
			this.offset = offset;
			this.shapeInput = createInputAndRegisterConnection(PortDataTypes.SHAPE, shape);
		}
		
		protected Shape create() {
			return new TranslateShape(
				getUuid(),
				get(shapeInput),
				offset
			);
		}
		
	}
	
}
