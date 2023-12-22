package com.ferreusveritas.node.transform;

import com.ferreusveritas.math.Matrix4X4;
import com.ferreusveritas.node.NodeLoader;
import com.ferreusveritas.scene.LoaderSystem;
import com.ferreusveritas.support.json.JsonObj;

import java.util.UUID;

public class Matrix extends Transform {
	
	public static final String TYPE = "matrix";
	
	private final Matrix4X4 data;
	
	private Matrix(UUID uuid, Matrix4X4 matrix) {
		super(uuid);
		this.data = matrix;
	}
	
	@Override
	public Matrix4X4 getMatrix() {
		return data;
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set("data", data);
	}
	
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder {
		
		private UUID uuid = null;
		private Matrix4X4 data = Matrix4X4.IDENTITY;
		
		public Builder uuid(UUID uuid) {
			this.uuid = uuid;
			return this;
		}
		
		public Builder matrix(Matrix4X4 data) {
			this.data = data;
			return this;
		}
		
		
		public Matrix build() {
			return new Matrix(uuid, data);
		}
		
	}
	
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends NodeLoader {
		
		private final Matrix4X4 data;
		
		public Loader(LoaderSystem loaderSystem, JsonObj src) {
			super(loaderSystem, src);
			this.data = src.getObj("data").map(Matrix4X4::new).orElse(Matrix4X4.IDENTITY);
		}
		
		@Override
		public Matrix load(LoaderSystem loaderSystem) {
			return new Matrix(getUuid(), data);
		}
		
	}
	
}
