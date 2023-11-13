package com.ferreusveritas.shapes;

import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.model.QSP;
import com.ferreusveritas.model.SimpleMeshModel;

import java.util.Optional;

public class ModelShape implements Shape {
	
	private final SimpleMeshModel model;
	private final QSP qsp;
	private final AABBI aabb;
	
	public ModelShape(SimpleMeshModel model) {
		this.model = model;
		this.aabb = model.getAABB().toAABBI();
		this.qsp = model.calculateQSP();
	}
	
	@Override
	public Optional<AABBI> getAABB() {
		return Optional.of(aabb);
	}
	
	@Override
	public boolean isInside(Vec3I pos) {
		return qsp.pointIsInside(pos.toVecD());
	}
}
