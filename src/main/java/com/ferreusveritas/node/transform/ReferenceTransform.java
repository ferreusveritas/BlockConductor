package com.ferreusveritas.node.transform;

import com.ferreusveritas.math.Matrix4X4;
import com.ferreusveritas.node.NodeLoader;
import com.ferreusveritas.scene.LoaderSystem;
import com.ferreusveritas.support.json.JsonObj;

import java.util.UUID;

public class ReferenceTransform extends Transform {

	public static final String TYPE = "reference";
	public static final String REF = "ref";
	
	private final Transform ref;
	private final Matrix4X4 matrix;
	
	private ReferenceTransform(UUID uuid, Transform ref) {
		super(uuid);
		this.ref = ref;
		this.matrix = ref.getMatrix();
	}
	
	@Override
	public Matrix4X4 getMatrix() {
		return matrix;
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(REF, ref.getUuid().toString());
	}
	
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Builder {
		
		private UUID uuid;
		private Transform ref;
		
		public Builder uuid(UUID uuid) {
			this.uuid = uuid;
			return this;
		}
		
		public Builder ref(Transform ref) {
			this.ref = ref;
			return this;
		}
		
		public ReferenceTransform build() {
			return new ReferenceTransform(uuid, ref);
		}
	}
	
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends NodeLoader {
		
		private final UUID ref;
		
		public Loader(LoaderSystem loaderSystem, JsonObj src) {
			super(loaderSystem, src);
			this.ref = src.getString(REF).map(UUID::fromString).orElseThrow(missing(REF));
		}
		
		@Override
		public ReferenceTransform load(LoaderSystem loaderSystem) {
			NodeLoader loader = loaderSystem.getLoader(this.ref).orElseThrow(() -> new IllegalStateException("Missing reference: " + this.ref + " in " + getUuid()));
			Transform t = loader.load(loaderSystem, Transform.class).orElseThrow(wrongType(REF));
			return new ReferenceTransform(getUuid(), t);
		}
		
	}
	
}
