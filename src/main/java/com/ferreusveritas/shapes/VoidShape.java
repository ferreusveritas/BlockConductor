package com.ferreusveritas.shapes;

import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.support.json.JsonObj;

import java.util.Optional;

/**
 * A shape that represents no blocks in the world.
 */
public class VoidShape extends Shape {
	
	public static final String TYPE = "void";
	
	public VoidShape() {
		super();
	}
	
	public VoidShape(JsonObj src) {
		super(src);
	}
	
	@Override
	public Optional<AABBI> getAABB() {
		return Optional.empty();
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public boolean isInside(Vec3I pos) {
		return false;
	}
	
}
