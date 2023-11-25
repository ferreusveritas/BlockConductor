package com.ferreusveritas.shapes;

import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.InvalidJsonProperty;
import com.ferreusveritas.support.json.JsonObj;

import java.util.List;
import java.util.Optional;

/**
 * Remove the presence of the second shape from the first shape with a Difference (-) operation
 */
public class DifferenceShape extends Shape {
	
	public static final String TYPE = "difference";
	
	private final List<Shape> shapes;
	
	public DifferenceShape(Scene scene, Shape ... shapes) {
		this(scene, List.of(shapes));
	}
	
	public DifferenceShape(Scene scene, List<Shape> shapes) {
		super(scene);
		this.shapes = shapes;
		validate();
	}
	
	public DifferenceShape(Scene scene, JsonObj src) {
		super(scene, src);
		this.shapes = src.getList("shapes").toImmutableList(scene::createShape);
	}
	
	private void validate() {
		if(shapes.isEmpty()) {
			throw new IllegalArgumentException("UnionShape must have at least one shape");
		}
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public Optional<AABBI> getAABB() {
		return shapes.get(0).getAABB();
	}
	
	@Override
	public boolean isInside(Vec3I pos) {
		if(!shapes.get(0).isInside(pos)) {
			return false;
		}
		for(int i = 1; i < shapes.size(); i++) {
			if(shapes.get(i).isInside(pos)) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set("shapes", JsonObj.newList(shapes));
	}
	
}
