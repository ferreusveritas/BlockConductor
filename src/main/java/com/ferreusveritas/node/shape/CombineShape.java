package com.ferreusveritas.node.shape;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.node.NodeLoader;
import com.ferreusveritas.node.shape.support.CombineOperation;
import com.ferreusveritas.scene.LoaderSystem;
import com.ferreusveritas.support.json.JsonObj;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
	
	private CombineShape(UUID uuid, CombineOperation operation, List<Shape> shapes) {
		super(uuid);
		this.operation = operation;
		this.shapes = shapes;
		this.bounds = calculateBounds(shapes);
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
	public String getType() {
		return TYPE;
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
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(OPERATION, operation)
			.set(SHAPES, JsonObj.newList(shapes));
	}
	
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder {
		
		private UUID uuid = null;
		private CombineOperation operation = CombineOperation.ADD;
		private final List<Shape> shapes = new ArrayList<>();
		
		public Builder uuid(UUID uuid) {
			this.uuid = uuid;
			return this;
		}
		
		public Builder operation(CombineOperation operation) {
			this.operation = operation;
			return this;
		}
		
		public Builder add(List<Shape> shapes) {
			this.shapes.addAll(shapes);
			return this;
		}
		
		public Builder add(Shape... shapes) {
			add(List.of(shapes));
			return this;
		}
		
		public CombineShape build() {
			return new CombineShape(uuid, operation, shapes);
		}
		
	}
	
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends NodeLoader {
		
		private final CombineOperation operation;
		private final List<NodeLoader> shapes;
		
		public Loader(LoaderSystem loaderSystem, UUID uuid, JsonObj src) {
			super(uuid);
			this.operation = src.getString(OPERATION).flatMap(CombineOperation::of).orElseThrow(() -> new IllegalArgumentException("operation is required"));
			this.shapes = src.getList(SHAPES).toImmutableList(loaderSystem::createLoader);
		}
		
		@Override
		public Shape load(LoaderSystem loaderSystem) {
			List<Shape> s = shapes.stream().map(l -> l.load(loaderSystem, Shape.class).orElseThrow()).toList();
			return new CombineShape(getUuid(), operation, s);
		}
		
	}
	
}
