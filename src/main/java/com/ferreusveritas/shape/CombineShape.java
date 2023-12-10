package com.ferreusveritas.shape;

import com.ferreusveritas.shape.support.CombineOperation;
import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

import java.util.List;

/**
 * CombineShape is an abstract Shape that combines the values of multiple Shapes together using a CombineOperation
 */
public class CombineShape extends Shape {
	
	public static final String TYPE = "combine";
	public static final String OPERATION = "operation";
	public static final String SHAPES = "shapes";
	
	private final CombineOperation operation;
	private final List<Shape> shapes;
	private final AABBD bounds;
	
	public CombineShape(Scene scene, CombineOperation operation, Shape... shapes) {
		this(scene, operation, List.of(shapes));
	}
	
	public CombineShape(Scene scene, CombineOperation operation, List<Shape> shapes) {
		super(scene);
		this.operation = operation;
		this.shapes = shapes;
		this.bounds = createBounds(shapes);
	}
	
	public CombineShape(Scene scene, JsonObj src) {
		super(scene, src);
		this.operation = src.getString(OPERATION).flatMap(CombineOperation::of).orElseThrow(() -> new IllegalArgumentException("operation is required"));
		this.shapes = src.getList(SHAPES).toImmutableList(scene::createShape);
		this.bounds = createBounds(shapes);
	}
	
	private AABBD createBounds(List<Shape> shapes) {
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
	public String getType() {
		return TYPE;
	}
	
	@Override
	public double getVal(Vec3D pos) {
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
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(OPERATION, operation)
			.set(SHAPES, JsonObj.newList(shapes));
	}
	
}
