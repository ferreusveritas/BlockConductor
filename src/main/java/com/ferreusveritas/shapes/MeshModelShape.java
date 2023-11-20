package com.ferreusveritas.shapes;

import com.ferreusveritas.math.*;
import com.ferreusveritas.model.*;
import com.ferreusveritas.support.json.InvalidJsonProperty;
import com.ferreusveritas.support.json.JsonObj;
import com.ferreusveritas.math.Matrix4X4;
import com.ferreusveritas.transform.MatrixTransform;
import com.ferreusveritas.transform.Transform;
import com.ferreusveritas.transform.TransformFactory;

import java.util.Optional;

/**
 * A Shape that uses a SimpleMeshModel to determine if a point is inside a model.
 */
public class MeshModelShape extends Shape {
	
	public static final String TYPE = "mesh_model";
	
	private final String resource;
	private final Transform transform;
	
	private final QSPModel qsp;
	private final Matrix4X4 matrix;
	private final AABBI aabb;
	
	private static QSPModel load(String resource) {
		FullMeshModel meshModel = ModelLoader.load(resource).orElseThrow(() -> new InvalidJsonProperty("Unable to load model: " + resource));
		return meshModel.toSimpleMeshModel().calculateQSP();
	}
	
	public MeshModelShape(String resource, Transform transform) {
		this.resource = resource;
		this.transform = transform;
		this.qsp = load(resource);
		this.matrix = transform.getMatrix().invert();
		this.aabb = calcAABB(qsp, matrix);
	}
	
	public MeshModelShape(String resource) {
		this(resource, new MatrixTransform());
	}
	
	public MeshModelShape(JsonObj src) {
		super(src);
		this.resource = src.getString("resource").orElseThrow(() -> new InvalidJsonProperty("Missing resource"));
		this.transform = src.getObj("transform").map(TransformFactory::create).orElseGet(MatrixTransform::new);
		this.qsp = load(resource);
		this.matrix = transform.getMatrix().invert();
		this.aabb = calcAABB(qsp, matrix);
	}
	
	@Override
	public String getType() {
		return TYPE;
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
		vec = matrix.transform(vec);
		return qsp.pointIsInside(vec);
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set("resource", resource)
			.set("transform", transform);
	}
	
}
