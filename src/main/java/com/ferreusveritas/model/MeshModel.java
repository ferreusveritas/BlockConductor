package com.ferreusveritas.model;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.InvalidJsonProperty;
import com.ferreusveritas.support.json.JsonObj;

import java.util.List;
import java.util.Optional;

public class MeshModel extends Model {
	
	public static final String TYPE = "mesh";
	
	private final String resource;
	private final String object;
	private final List<SimpleFace> faces;
	private final AABBD aabb;
	private final QSPModel qsp;
	
	public MeshModel(Scene scene, String resource, String object) {
		super(scene);
		this.resource = resource;
		this.object = object;
		ObjModel objModel = ObjModelLoader.load(resource, object).orElseThrow(() -> new InvalidJsonProperty("Unable to load model: " + resource));
		this.faces = objModel.getFaces().stream().map(FullFace::toSimpleFace).toList();
		this.aabb = calculateAABB().orElseThrow(() -> new InvalidJsonProperty("SimpleMeshModel AABB is null"));
		this.qsp = calculateQSP();
	}
	
	public MeshModel(Scene scene, JsonObj src) {
		super(scene, src);
		this.resource = src.getString("resource").orElseThrow(() -> new InvalidJsonProperty("Missing resource"));
		this.object = src.getString("object").orElse(null);
		ObjModel objModel = ObjModelLoader.load(resource, object).orElseThrow(() -> new InvalidJsonProperty("Unable to load model: " + resource));
		this.faces = objModel.getFaces().stream().map(FullFace::toSimpleFace).toList();
		this.aabb = calculateAABB().orElseThrow(() -> new InvalidJsonProperty("SimpleMeshModel AABB is null"));
		this.qsp = calculateQSP();
	}
	
	private QSPModel calculateQSP() {
		return QSPModel.load(this);
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	public SimpleFace getFace(int index) {
		return faces.get(index);
	}
	
	public List<SimpleFace> getFaces() {
		return faces;
	}
	
	public int getFaceCount() {
		return faces.size();
	}
	
	public AABBD getAABB() {
		return aabb;
	}
	
	private Optional<AABBD> calculateAABB() {
		if(getFaceCount() == 0) {
			return Optional.empty();
		}
		AABBD box = getFace(0).getAABB();
		for(int i = 1; i < getFaceCount(); i++) {
			AABBD faceAABB = getFace(i).getAABB();
			box = box.union(faceAABB);
		}
		return Optional.of(box);
	}
	
	@Override
	public boolean pointIsInside(Vec3D point) {
		return qsp.pointIsInside(point);
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set("resource", resource)
			.set("object", object);
	}
	
}
