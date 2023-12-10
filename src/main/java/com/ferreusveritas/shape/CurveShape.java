package com.ferreusveritas.shape;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.MathHelper;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;
import com.ferreusveritas.support.json.Jsonable;
import org.spongepowered.noise.exception.NoiseException;

import java.util.Comparator;
import java.util.List;

public class CurveShape extends Shape {
	
	public static final String TYPE = "curve";
	public static final String CONTROL_POINTS = "controlPoints";
	
	private final Shape shape;
	private final List<ControlPoint> controlPoints;
	private final AABBD bounds;
	
	public CurveShape(Scene scene, Shape shape, List<ControlPoint> controlPoints) {
		super(scene);
		this.shape = shape;
		this.controlPoints = sort(controlPoints);
		this.bounds = calculateBounds();
		validate();
	}
	
	public CurveShape(Scene scene, JsonObj src) {
		super(scene, src);
		this.shape = src.getObj(SHAPE).map(scene::createShape).orElseThrow(missing(SHAPE));
		this.controlPoints = sort(src.getList(CONTROL_POINTS).toImmutableList(ControlPoint::new));
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
	
}
