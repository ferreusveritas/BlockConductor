package com.ferreusveritas.node.shape;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.node.DataSpan;
import com.ferreusveritas.node.ports.*;
import com.ferreusveritas.node.NodeRegistryData;
import com.ferreusveritas.node.shape.support.CombineOperation;
import com.ferreusveritas.node.values.EnumNodeValue;

import java.util.List;
import java.util.UUID;

/**
 * CombineShape is an abstract Shape that combines the values of multiple Shapes together using a CombineOperation
 */
public class CombineShape extends Shape {
	
	public static final String OPERATION = "operation";
	public static final NodeRegistryData REGISTRY_DATA = new NodeRegistryData.Builder()
		.majorType(SHAPE)
		.minorType("combine")
		.loaderClass(Loader.class)
		.sceneObjectClass(CombineShape.class)
		.value(new EnumNodeValue.Builder(OPERATION).values(CombineOperation.class).def(CombineOperation.OR).build())
		.port(new PortDescription(PortDirection.IN, PortDataTypes.SHAPE, DataSpan.MULTIPLE))
		.port(new PortDescription(PortDirection.OUT, PortDataTypes.SHAPE))
		.build();
	
	private final CombineOperation operation;
	private final List<Shape> shapes;
	private final AABBD bounds;
	
	private CombineShape(UUID uuid, CombineOperation operation, List<Shape> shapes) {
		super(uuid);
		this.operation = operation;
		this.shapes = shapes;
		this.bounds = calculateBounds(shapes);
	}
	
	@Override
	public NodeRegistryData getRegistryData() {
		return REGISTRY_DATA;
	}
	
	private AABBD calculateBounds(List<Shape> shapes) {
		return shapes.stream()
			.map(Shape::bounds)
			.reduce(operation::apply)
			.orElse(AABBD.INFINITE);
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
		int size = shapes.size();
		double accum = size > 0 ? shapes.get(0).getVal(pos) : 0.0;
		if(size == 1) {
			return operation.apply(accum, 0.0);
		}
		for(int i = 1; i < size; i++) {
			double val = shapes.get(i).getVal(pos);
			accum = operation.apply(accum, val);
		}
		return accum;
	}
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends ShapeLoaderNode {
		
		private final CombineOperation operation;
		private final List<InputPort<Shape>> shapeInputs;
		
		@JsonCreator
		public Loader(
			@JsonProperty(UID) UUID uuid,
			@JsonProperty(OPERATION) CombineOperation operation,
			@JsonProperty(SHAPE) List<PortAddress> shapeAddresses
		) {
			super(uuid);
			this.operation = operation;
			this.shapeInputs = createInputsAndRegisterConnections(PortDataTypes.SHAPE, shapeAddresses);
		}
		
		protected Shape create() {
			return new CombineShape(
				getUuid(),
				operation,
				shapeInputs.stream().map(this::get).toList()
			);
		}
		
	}
	
}
