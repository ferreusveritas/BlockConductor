package com.ferreusveritas.node.shape;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.node.model.Model;
import com.ferreusveritas.node.NodeLoader;
import com.ferreusveritas.scene.LoaderSystem;
import com.ferreusveritas.support.json.JsonObj;

import java.util.UUID;

/**
 * A Shape that uses a Model
 */
public class ModelShape extends Shape {
	
	public static final String TYPE = "model";
	public static final String MODEL = "model";
	
	private final Model model;
	private final AABBD bounds;
	
	private ModelShape(UUID uuid, Model model) {
		super(uuid);
		this.model = model;
		this.bounds = model.getAABB();
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public AABBD bounds() {
		return bounds;
	}
	
	@Override
	public double getVal(Vec3D pos) {
		return bounds.contains(pos) && model.pointIsInside(pos) ? 1.0 : 0.0;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(MODEL, model);
	}
	
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder {
		
		private UUID uuid = null;
		private Model model;
		
		public Builder uuid(UUID uuid) {
			this.uuid = uuid;
			return this;
		}
		
		public Builder model(Model model) {
			this.model = model;
			return this;
		}
		
		public ModelShape build() {
			if (model == null){
				throw new IllegalStateException("Model is null");
			}
			return new ModelShape(uuid, model);
		}
		
	}
	
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends NodeLoader {
		
		private final NodeLoader model;
		
		public Loader(LoaderSystem loaderSystem, JsonObj src) {
			super(loaderSystem, src);
			this.model = loaderSystem.loader(src, MODEL);
		}
		
		@Override
		public Shape load(LoaderSystem loaderSystem) {
			Model m = model.load(loaderSystem, Model.class).orElseThrow(wrongType(MODEL));
			return new ModelShape(getUuid(), m);
		}
		
	}
	
}
