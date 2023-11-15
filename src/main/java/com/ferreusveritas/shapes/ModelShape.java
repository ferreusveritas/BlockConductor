package com.ferreusveritas.shapes;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.model.QSP;
import com.ferreusveritas.model.SimpleMeshModel;

import java.util.Optional;

/**
 * A Shape that uses a SimpleMeshModel to determine if a point is inside a model.
 */
public class ModelShape implements Shape {
	
	private final QSP qsp;
	private final AABBD aabb;
	
	public ModelShape(SimpleMeshModel model) {
		this.aabb = model.getAABB();
		this.qsp = model.calculateQSP();
	}
	
	@Override
	public Optional<AABBI> getAABB() {
		return Optional.of(aabb.toAABBI());
	}
	
	@Override
	public boolean isInside(Vec3I pos) {
		return qsp.pointIsInside(pos.toVecD());
	}
	
}
