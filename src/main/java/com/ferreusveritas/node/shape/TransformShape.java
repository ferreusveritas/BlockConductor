package com.ferreusveritas.node.shape;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Matrix4X4;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.node.ports.*;
import com.ferreusveritas.node.NodeRegistryData;
import com.ferreusveritas.node.transform.Transform;

import java.util.UUID;

import static com.ferreusveritas.node.transform.Transform.TRANSFORM;

public class TransformShape extends Shape {
	
	public static final NodeRegistryData REGISTRY_DATA = new NodeRegistryData.Builder()
		.majorType(SHAPE)
		.minorType("transform")
		.loaderClass(Loader.class)
		.sceneObjectClass(TransformShape.class)
		.port(new PortDescription(PortDirection.IN, PortDataTypes.SHAPE))
		.port(new PortDescription(PortDirection.IN, PortDataTypes.TRANSFORM))
		.port(new PortDescription(PortDirection.OUT, PortDataTypes.SHAPE))
		.build();
	
	private final Shape shape;
	private final Transform transform;
	private final Matrix4X4 matrix;
	private final AABBD bounds;
	
	private TransformShape(UUID uuid, Shape shape, Transform transform) {
		super(uuid);
		this.shape = shape;
		this.transform = transform;
		this.matrix = transform.getMatrix().invert();
		this.bounds = calculateBounds(shape);
	}
	
	@Override
	public NodeRegistryData getRegistryData() {
		return REGISTRY_DATA;
	}
	
	private AABBD calculateBounds(Shape shape) {
		return shape.bounds().transform(transform.getMatrix());
	}
	
	@Override
	public AABBD bounds() {
		return bounds;
	}
	
	@Override
	public double getVal(Vec3D pos) {
		if(bounds.contains(pos)) {
			Vec3D transPos = matrix.transform(pos);
			if(shape.bounds().contains(transPos)) {
				return shape.getVal(transPos);
			}
		}
		return 0;
	}
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends ShapeLoaderNode {
		
		private final InputPort<Shape> shapeInput;
		private final InputPort<Transform> transformInput;
		
		@JsonCreator
		public Loader(
			@JsonProperty(UID) UUID uuid,
			@JsonProperty(SHAPE) PortAddress shapeAddress,
			@JsonProperty(TRANSFORM) PortAddress transformAddress
		) {
			super(uuid);
			shapeInput = createInputAndRegisterConnection(PortDataTypes.SHAPE, shapeAddress);
			transformInput = createInputAndRegisterConnection(PortDataTypes.TRANSFORM, transformAddress);
		}
		
		protected Shape create() {
			return new TransformShape(
				getUuid(),
				get(shapeInput),
				get(transformInput)
			);
		}
		
	}
	
}
