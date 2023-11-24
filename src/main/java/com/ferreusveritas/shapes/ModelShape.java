package com.ferreusveritas.shapes;

import com.ferreusveritas.math.*;
import com.ferreusveritas.model.*;
import com.ferreusveritas.scene.Scene;
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
public class ModelShape extends Shape {
	
	public static final String TYPE = "mesh_model";
	
	private final Model model;
	private final Transform transform;
	private final Matrix4X4 matrix;
	private final AABBI aabb;
	
	public ModelShape(Scene scene, Model model, Transform transform) {
		super(scene);
		this.model = model;
		this.transform = transform;
		this.matrix = transform.getMatrix().invert();
		this.aabb = calcAABB(model, matrix);
	}
	
	public ModelShape(Scene scene, JsonObj src) {
		super(scene, src);
		this.model = src.getObj("model").map(scene::createModel).orElseThrow(() -> new InvalidJsonProperty("Missing model"));
		this.transform = src.getObj("transform").map(TransformFactory::create).orElseGet(MatrixTransform::new);
		this.matrix = transform.getMatrix().invert();
		this.aabb = calcAABB(model, matrix);
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	private AABBI calcAABB(Model model, Matrix4X4 transform) {
		AABBD tempAABB = model.getAABB().transform(transform);
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
		return model.pointIsInside(vec);
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set("model", model)
			.set("transform", transform);
	}
	
}
