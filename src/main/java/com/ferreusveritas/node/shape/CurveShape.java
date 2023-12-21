package com.ferreusveritas.node.shape;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.MathHelper;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.node.NodeLoader;
import com.ferreusveritas.scene.LoaderSystem;
import com.ferreusveritas.support.json.JsonObj;
import com.ferreusveritas.support.json.Jsonable;
import org.spongepowered.noise.exception.NoiseException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class CurveShape extends Shape {
	
	public static final String TYPE = "curve";
	public static final String CONTROL_POINTS = "controlPoints";
	
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
	public String getType() {
		return TYPE;
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
	
	private double process(double in) {
		int size = controlPoints.size();
		int indexPos;
		for(indexPos = 0; indexPos < size && (in >= (controlPoints.get(indexPos)).in); ++indexPos) {}
		
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
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(CONTROL_POINTS, JsonObj.newList(controlPoints))
			.set(SHAPE, shape);
	}
	
	public record ControlPoint(
		double in,
		double out
	) implements Jsonable {
		
		public ControlPoint(JsonObj src) {
			this(
				src.getDouble("in").orElse(0.0),
				src.getDouble("out").orElse(0.0)
			);
		}
		
		@Override
		public JsonObj toJsonObj() {
			return JsonObj.newMap()
				.set("in", in)
				.set("out", out);
		}
		
	}
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder {
		
		private UUID uuid = null;
		private Shape shape = VoidShape.VOID;
		private final List<ControlPoint> controlPoints = new ArrayList<>();
		
		public Builder uuid(UUID uuid) {
			this.uuid = uuid;
			return this;
		}
		
		public Builder shape(Shape shape) {
			this.shape = shape;
			return this;
		}
		
		public Builder add(List<ControlPoint> controlPoints) {
			this.controlPoints.addAll(controlPoints);
			return this;
		}
		
		public Builder add(ControlPoint... controlPoints) {
			add(List.of(controlPoints));
			return this;
		}
		
		public CurveShape build() {
			return new CurveShape(uuid, shape, controlPoints);
		}
		
	}
	
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends NodeLoader {
		
		private final NodeLoader shape;
		private final List<ControlPoint> controlPoints;
		
		public Loader(LoaderSystem loaderSystem, UUID uuid, JsonObj src) {
			super(uuid);
			this.shape = loaderSystem.loader(src, SHAPE);
			this.controlPoints = sort(src.getList(CONTROL_POINTS).toImmutableList(ControlPoint::new));
		}
		
		@Override
		public Shape load(LoaderSystem loaderSystem) {
			Shape s = shape.load(loaderSystem, Shape.class).orElseThrow(wrongType(SHAPE));
			return new CurveShape(getUuid(), s, controlPoints);
		}
		
	}
	
}
