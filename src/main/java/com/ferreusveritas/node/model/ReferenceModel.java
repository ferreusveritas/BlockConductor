package com.ferreusveritas.node.model;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.node.NodeLoader;
import com.ferreusveritas.scene.LoaderSystem;
import com.ferreusveritas.support.json.JsonObj;

import java.util.UUID;

public class ReferenceModel extends Model {

	public static final String TYPE = "reference";
	public static final String REF = "ref";
	
	private final Model ref;
	
	public ReferenceModel(UUID uuid, Model ref) {
		super(uuid);
		this.ref = ref;
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public boolean pointIsInside(Vec3D point) {
		return ref.pointIsInside(point);
	}
	
	@Override
	public AABBD getAABB() {
		return ref.getAABB();
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(REF, ref);
	}
	
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends NodeLoader {
		
		private final UUID ref;
		
		public Loader(LoaderSystem loaderSystem, UUID uuid, JsonObj src) {
			super(uuid);
			this.ref = src.getString(REF).map(UUID::fromString).orElseThrow(missing(REF));
		}
		
		@Override
		public Model load(LoaderSystem loaderSystem) {
			NodeLoader loader = loaderSystem.getLoader(this.ref).orElseThrow(() -> new IllegalStateException("Missing reference: " + this.ref + " in " + getUuid()));
			Model s = loader.load(loaderSystem, Model.class).orElseThrow(wrongType(REF));
			return new ReferenceModel(getUuid(), s);
		}
		
	}
	
}
