package com.ferreusveritas.shapes;

import com.ferreusveritas.math.*;
import com.ferreusveritas.model.QSPModel;
import com.ferreusveritas.model.SimpleFace;
import com.ferreusveritas.model.SimpleMeshModel;

import java.util.Optional;

/**
 * A Shape that uses a SimpleMeshModel to determine if a point is inside a model.
 */
public class MeshModelShape implements Shape {
	
	private final QSPModel qsp;
	private final Matrix4X4 transform;
	private final AABBI aabb;
	
	public MeshModelShape(QSPModel qsp, Matrix4X4 transform) {
		this.qsp = qsp;
		this.transform = transform.invert();
		this.aabb = calcAABB(qsp, transform);
	}
	
	public MeshModelShape(QSPModel qsp) {
		this(qsp, Matrix4X4.IDENTITY);
	}
	
	private AABBI calcAABB(QSPModel qsp, Matrix4X4 transform) {
		SimpleMeshModel smm = qsp.getAABB().toMeshModel();
		AABBD tempAABB = null;
		for(SimpleFace face : smm.getFaces()) {
			for(Vec3D vertex : face.getVertices()) {
				vertex = transform.transform(vertex);
				AABBD vertAABB = new AABBD(vertex, vertex);
				tempAABB = AABBD.union(tempAABB, vertAABB);
			}
		}
		if(tempAABB == null) {
			throw new IllegalArgumentException("Unable to calculate AABB for ModelShape");
		}
		return tempAABB.toAABBI();
	}
	
	@Override
	public Optional<AABBI> getAABB() {
		return Optional.of(aabb);
	}
	
	@Override
	public boolean isInside(Vec3I pos) {
		Vec3D vec = pos.toVecD();
		vec = transform.transform(vec);
		return qsp.pointIsInside(vec);
	}
	
}
