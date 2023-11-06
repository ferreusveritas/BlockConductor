package com.ferreusveritas.shapeproviders;

import com.ferreusveritas.api.AABB;
import com.ferreusveritas.api.VecI;

public interface ShapeProvider {
	AABB getAABB();
	boolean isInside(VecI pos);
}
