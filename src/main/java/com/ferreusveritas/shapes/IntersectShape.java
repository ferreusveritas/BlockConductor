package com.ferreusveritas.shapes;

import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

import java.util.List;
import java.util.Optional;

/**
 * Combine two or more shapes in such a way that only the blocks common to all are present.  This is an intersect (∩) operation
 */
public class IntersectShape extends Shape {
	
	public static final String TYPE = "intersect";
	
	private final List<Shape> shapes;
	
	public IntersectShape(Scene scene, Shape... shapes) {
		this(scene, List.of(shapes));
	}
	
	public IntersectShape(Scene scene, List<Shape> shapes) {
		super(scene);
		this.shapes = shapes;
		validate();
	}
	
	public IntersectShape(Scene scene, JsonObj src) {
		super(scene, src);
		this.shapes = src.getList("shapes").toImmutableList(scene::createShape);
		validate();
	}
	
	private void validate() {
		if(shapes.isEmpty()) {
			throw new IllegalArgumentException("IntersectShape must have at least one shape");
		}
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public Optional<AABBI> getAABB() {
		AABBI aabb = null;
		for (Shape shape : shapes) {
			if (aabb == null) {
				aabb = shape.getAABB().orElse(null);
			} else {
				AABBI next = shape.getAABB().orElse(null);
				if(next == null) {
					return Optional.empty();
				}
				aabb = aabb.intersect(next).orElse(null);
				if(aabb == null) {
					return Optional.empty();
				}
			}
		}
		return Optional.ofNullable(aabb);
	}
	
	@Override
	public boolean isInside(Vec3I pos) {
		return false;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set("shapes", JsonObj.newList(shapes));
	}
	
}
