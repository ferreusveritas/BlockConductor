package com.ferreusveritas.node.shape;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.MathHelper;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.node.ports.*;
import com.ferreusveritas.node.NodeRegistryData;

import java.util.UUID;

/**
 * BlendShape is a type of Shape that blends the values of two shapes together using a blend shape as a mask.
 */
public class BlendShape extends Shape {
	
	private static final String SHAPE_IN1 = "shape1";
	private static final String SHAPE_IN2 = "shape2";
	private static final String BLEND = "blend";
	public static final NodeRegistryData REGISTRY_DATA = new NodeRegistryData.Builder()
		.majorType(SHAPE)
		.minorType(BLEND)
		.loaderClass(Loader.class)
		.sceneObjectClass(BlendShape.class)
		.port(new PortDescription(SHAPE_IN1, PortDirection.IN, PortDataTypes.SHAPE))
		.port(new PortDescription(SHAPE_IN2, PortDirection.IN, PortDataTypes.SHAPE))
		.port(new PortDescription(BLEND, PortDirection.IN, PortDataTypes.SHAPE))
		.port(new PortDescription(PortDirection.OUT, PortDataTypes.SHAPE))
		.build();
	
	private final Shape shape1;
	private final Shape shape2;
	private final Shape blend;
	private final AABBD bounds;
	
	private BlendShape(UUID uuid, Shape shape1, Shape shape2, Shape blend) {
		super(uuid);
		this.shape1 = shape1;
		this.shape2 = shape2;
		this.blend = blend;
		this.bounds = calculateBounds();
	}
	
	@Override
	public NodeRegistryData getRegistryData() {
		return REGISTRY_DATA;
	}
	
	private AABBD calculateBounds() {
		return AABBD.union(shape1.bounds(), shape2.bounds());
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
		double blendVal = blend.getVal(pos);
		if(blendVal < 0.0){
			return shape1.getVal(pos);
		}
		if(blendVal > 1.0){
			return shape2.getVal(pos);
		}
		double val1 = shape1.getVal(pos);
		double val2 = shape2.getVal(pos);
		return MathHelper.lerp(blendVal, val1, val2);
	}
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends ShapeLoaderNode {
		
		private final InputPort<Shape> shapeInput1;
		private final InputPort<Shape> shapeInput2;
		private final InputPort<Shape> blendInput;
		
		@JsonCreator
		public Loader(
			@JsonProperty(UID) UUID uuid,
			@JsonProperty(SHAPE_IN1) PortAddress shapeAddress1,
			@JsonProperty(SHAPE_IN2) PortAddress shapeAddress2,
			@JsonProperty(BLEND) PortAddress blendAddress
		) {
			super(uuid);
			this.shapeInput1 = createInputAndRegisterConnection(SHAPE_IN1, PortDataTypes.SHAPE, shapeAddress1);
			this.shapeInput2 = createInputAndRegisterConnection(SHAPE_IN2, PortDataTypes.SHAPE, shapeAddress2);
			this.blendInput = createInputAndRegisterConnection(BLEND, PortDataTypes.SHAPE, blendAddress);
		}
		
		@Override
		protected Shape create() {
			return new BlendShape(
				getUuid(),
				get(shapeInput1),
				get(shapeInput2),
				get(blendInput)
			);
		}
		
	}
	
}
