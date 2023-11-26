package com.ferreusveritas.image;

import com.ferreusveritas.math.MathHelper;
import com.ferreusveritas.math.RectI;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.InvalidJsonProperty;
import com.ferreusveritas.support.json.JsonObj;
import com.ferreusveritas.support.json.Jsonable;
import org.spongepowered.noise.exception.NoiseException;

import java.util.Comparator;
import java.util.List;

public class CurveImage extends Image {
	
	public static final String TYPE = "curve";
	
	private final Image image;
	private final List<ControlPoint> controlPoints;
	
	public CurveImage(Scene scene, Image image, List<ControlPoint> controlPoints) {
		super(scene);
		this.image = image;
		this.controlPoints = sort(controlPoints);
		validate();
	}
	
	public CurveImage(Scene scene, JsonObj src) {
		super(scene, src);
		this.image = src.getObj("image").map(scene::createImage).orElseThrow(() -> new InvalidJsonProperty("Missing image"));
		this.controlPoints = sort(src.getList("controlPoints").toImmutableList(ControlPoint::new));
		validate();
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
	public RectI bounds() {
		return image.bounds();
	}
	
	@Override
	public double getVal(int x, int y) {
		return process(image.getVal(x, y));
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
		return MathHelper.cubicInterp(
			controlPoints.get(index0).out,
			controlPoints.get(index1).out,
			controlPoints.get(index2).out,
			controlPoints.get(index3).out,
			alpha);
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set("image", image)
			.set("controlPoints", JsonObj.newList(controlPoints));
	}
	
	public static record ControlPoint(
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
