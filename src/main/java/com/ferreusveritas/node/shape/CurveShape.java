package com.ferreusveritas.node.shape;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.MathHelper;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.node.ports.*;
import com.ferreusveritas.node.NodeRegistryData;
import org.spongepowered.noise.exception.NoiseException;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class CurveShape extends Shape {
	
	public static final NodeRegistryData REGISTRY_DATA = new NodeRegistryData.Builder()
		.majorType(SHAPE)
		.minorType("curve")
		.loaderClass(Loader.class)
		.sceneObjectClass(CurveShape.class)
		.port(new PortDescription(PortDirection.OUT, PortDataTypes.SHAPE))
		.build();
	
	private final Shape shape;
	private final List<ControlPoint> controlPoints;
	private final AABBD bounds;
	
	private CurveShape(UUID uuid, Shape shape, List<ControlPoint> controlPoints) {
		super(uuid);
		this.shape = shape;
		this.controlPoints = sort(controlPoints);
		this.bounds = calculateBounds();
		validate();
	}
	
	@Override
	public NodeRegistryData getRegistryData() {
		return REGISTRY_DATA;
	}
	
	private AABBD calculateBounds() {
		return shape.bounds();
	}
	
	private static List<ControlPoint> sort(List<ControlPoint> controlPoints) {
		return controlPoints.stream().sorted(Comparator.comparingDouble(a -> a.in)).toList();
	}
	
	private void validate() {
		if (controlPoints.size() < 4) {
			throw new NoiseException("Curve module must have at least 4 control points");
		}
	}
	
	@Override
	public AABBD bounds() {
		return bounds;
	}
	
	@Override
	public double getVal(Vec3D pos) {
		if (!bounds.contains(pos)) {
			return 0.0;
		}
		return process(shape.getVal(pos));
	}
	
	private int findStartIndex(double in) {
		int size = controlPoints.size();
		for(int i = 0; i < size; ++i) {
			if (in <= (controlPoints.get(i)).in) {
				return i;
			}
		}
		return size - 1;
	}
	
	private double process(double in) {
		int size = controlPoints.size();
		int indexPos = findStartIndex(in);
		
		int lastIndex = size - 1;
		int index0 = MathHelper.clamp(indexPos - 2, 0, lastIndex);
		int index1 = MathHelper.clamp(indexPos - 1, 0, lastIndex);
		int index2 = MathHelper.clamp(indexPos, 0, lastIndex);
		int index3 = MathHelper.clamp(indexPos + 1, 0, lastIndex);
		if (index1 == index2) {
			return controlPoints.get(index1).out;
		}
		double input0 = controlPoints.get(index1).in;
		double input1 = controlPoints.get(index2).in;
		double alpha = (in - input0) / (input1 - input0);
		return MathHelper.cerp(
			controlPoints.get(index0).out,
			controlPoints.get(index1).out,
			controlPoints.get(index2).out,
			controlPoints.get(index3).out,
			alpha);
	}
	
	public record ControlPoint(
		double in,
		double out
	) {}
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends ShapeLoaderNode {
		
		private final List<ControlPoint> controlPoints;
		private final InputPort<Shape> shapeInput;
		
		@JsonCreator
		public Loader(
			@JsonProperty(UID) UUID uuid,
			@JsonProperty("controlPoints") List<ControlPoint> controlPoints,
			@JsonProperty(SHAPE) PortAddress shapeAddress
		) {
			super(uuid);
			this.shapeInput = createInputAndRegisterConnection(SHAPE, PortDataTypes.SHAPE, shapeAddress);
			this.controlPoints = sort(controlPoints);
		}
		
		protected Shape create() {
			return new CurveShape(
				getUuid(),
				get(shapeInput),
				controlPoints
			);
		}
		
	}
	
}
