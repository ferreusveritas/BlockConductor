package com.ferreusveritas.shapes;

import com.ferreusveritas.math.*;
import com.ferreusveritas.model.QSP;
import com.ferreusveritas.model.SimpleMeshModel;

import java.util.Optional;

/**
 * A Shape that uses a SimpleMeshModel to determine if a point is inside a model.
 */
public class ModelShape implements Shape {
	
	private final QSP qsp;
	private final AABBD aabb;
	private final Matrix4X4 transform;
	
	public ModelShape(SimpleMeshModel model, Matrix4X4 transform) {
		this.aabb = model.getAABB();
		this.qsp = model.calculateQSP();
		this.transform = transform;
	}
	
	public ModelShape(SimpleMeshModel model) {
		this(model, Matrix4X4.IDENTITY);
	}
	
	@Override
	public Optional<AABBI> getAABB() {
		// TODO: Transform the AABB by the transform matrix
		return Optional.of(aabb.toAABBI());
	}
	
	@Override
	public boolean isInside(Vec3I pos) {
		Vec3D vec = pos.toVecD();
		vec = transform.transform(vec);
		return qsp.pointIsInside(vec);
	}
	
}
