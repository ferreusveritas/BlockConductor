package com.ferreusveritas.shapes;

import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.support.json.JsonObj;

import java.util.Optional;

/**
 * A shape that represents all blocks in the world.
 */
public class UbiquitousShape extends Shape {
	
	public static final String TYPE = "ubiquitous";
	
	public UbiquitousShape() {
		super();
	}
	
	public UbiquitousShape(JsonObj src) {
		super(src);
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public Optional<AABBI> getAABB() {
		return Optional.of(AABBI.INFINITE);
	}
	
	@Override
	public boolean isInside(Vec3I pos) {
		return true;
	}
	
}
