package com.ferreusveritas.node.model;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.node.NodeLoader;
import com.ferreusveritas.node.model.support.*;
import com.ferreusveritas.scene.LoaderSystem;
import com.ferreusveritas.support.json.InvalidJsonProperty;
import com.ferreusveritas.support.json.JsonObj;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MeshModel extends Model {
	
	public static final String TYPE = "mesh";
	public static final String RESOURCE = "resource";
	public static final String OBJECT = "object";
	
	private final String resource;
	private final String part;
	private final List<SimpleFace> faces;
	private final AABBD aabb;
	private final QSPModel qsp;
	
	private MeshModel(UUID uuid, String resource, String part) {
		super(uuid);
		this.resource = resource;
		this.part = part;
		ObjModel objModel = ObjModelLoader.load(resource, part).orElseThrow(() -> new InvalidJsonProperty("Unable to load model: " + resource));
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
			.set(RESOURCE, resource)
			.set(OBJECT, part);
	}
	
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder {
		
		private UUID uuid = null;
		private String resource = null;
		private String part = null;
		
		public Builder uuid(UUID uuid) {
			this.uuid = uuid;
			return this;
		}
		
		public Builder resource(String resource) {
			this.resource = resource;
			return this;
		}
		
		public Builder part(String part) {
			this.part = part;
			return this;
		}
		
		public MeshModel build() {
			if(resource == null) {
				throw new IllegalStateException("resource is null");
			}
			return new MeshModel(uuid, resource, part);
		}
		
	}
	
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends NodeLoader {
		
		private final String resource;
		private final String object;
		
		public Loader(LoaderSystem loaderSystem, JsonObj src) {
			super(loaderSystem, src);
			this.resource = src.getString(RESOURCE).orElseThrow(() -> new InvalidJsonProperty("Missing resource"));
			this.object = src.getString(OBJECT).orElse(null);
		}
		
		@Override
		public Model load(LoaderSystem loaderSystem) {
			return new MeshModel(getUuid(), resource, object);
		}
		
	}
	
}
