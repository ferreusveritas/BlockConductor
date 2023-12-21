package com.ferreusveritas.node.shape;

import com.ferreusveritas.math.AABBD;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.node.NodeLoader;
import com.ferreusveritas.scene.LoaderSystem;
import com.ferreusveritas.support.json.JsonObj;

import java.util.UUID;

/**
 * A Shape that references another Shape by UUID
 */
public class ReferenceShape extends Shape {
	
	public static final String TYPE = "reference";
	public static final String REF = "ref";
	
	private final Shape ref;
	private final AABBD bounds;
	
	private ReferenceShape(UUID uuid, Shape ref) {
		super(uuid);
		this.ref = ref;
		this.bounds = calculateBounds();
	}
	
	private AABBD calculateBounds() {
		return ref.bounds();
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
		return ref.getVal(pos);
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
		private Shape ref;
		
		public Builder uuid(UUID uuid) {
			this.uuid = uuid;
			return this;
		}
		
		public Builder ref(Shape ref) {
			this.ref = ref;
			return this;
		}
		
		public ReferenceShape build() {
			return new ReferenceShape(uuid, ref);
		}
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
		public Shape load(LoaderSystem loaderSystem) {
			NodeLoader loader = loaderSystem.getLoader(this.ref).orElseThrow(() -> new IllegalStateException("Missing reference: " + this.ref + " in " + getUuid()));
			Shape s = loader.load(loaderSystem, Shape.class).orElseThrow(wrongType(REF));
			return new ReferenceShape(getUuid(), s);
		}
		
	}
	
}
