package com.ferreusveritas.shape;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Matrix4X4;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;
import com.ferreusveritas.transform.Transform;
import com.ferreusveritas.transform.TransformFactory;

public class TransformShape extends Shape {
	
	public static final String TYPE = "transform";
	public static final String TRANSFORM = "transform";
	
	private final Shape shape;
	private final Transform transform;
	private final Matrix4X4 matrix;
	private final AABBD bounds;
	
	public TransformShape(Scene scene, Shape shape, Transform transform) {
		super(scene);
		this.shape = shape;
		this.transform = transform;
		this.matrix = transform.getMatrix().invert();
		this.bounds = calculateBounds(shape);
	}
	
	public TransformShape(Scene scene, JsonObj src) {
		super(scene, src);
		this.shape = src.getObj(SHAPE).map(scene::createShape).orElseThrow(missing(SHAPE));
		this.transform = src.getObj(TRANSFORM).map(TransformFactory::create).orElseThrow(missing(TRANSFORM));
		this.matrix = transform.getMatrix().invert();
		this.bounds = calculateBounds(shape);
	}
	
	private AABBD calculateBounds(Shape shape) {
		return shape.bounds().transform(transform.getMatrix());
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
		Vec3D transPos = matrix.transform(pos);
		if(shape.bounds().contains(transPos)) {
			return shape.getVal(transPos);
		}
		return 0;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(TRANSFORM, transform)
			.set(SHAPE, shape);
	}
	
}
