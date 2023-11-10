package com.ferreusveritas.shapes;

import com.ferreusveritas.math.AABB;
import com.ferreusveritas.math.VecI;

import java.util.Optional;

public interface Shape {
	
	/**
	 * Get the bounding box of the shape
	 * @return the bounding box of the shape. Empty if the shape has no bounding box
	 */
	Optional<AABB> getAABB();
	
	/**
	 * Check if the shape intersects at the given position
	 * @param pos the area to check
	 * @return true if the shape intersects
	 */
	boolean isInside(VecI pos);
}
