package com.ferreusveritas.node.provider;

import com.ferreusveritas.api.Request;
import com.ferreusveritas.block.Blocks;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.node.NodeLoader;
import com.ferreusveritas.scene.LoaderSystem;
import com.ferreusveritas.support.json.JsonObj;

import java.util.Optional;
import java.util.UUID;

/**
 * A block provider that references another block provider by its UUID.
 */
public class ReferenceBlockProvider extends BlockProvider {
	
	public static final String TYPE = "reference";
	public static final String REF = "ref";
	
	private final BlockProvider ref;
	
	private ReferenceBlockProvider(UUID uuid, BlockProvider ref) {
		super(uuid);
		this.ref = ref;
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public Optional<Blocks> getBlocks(Request request) {
		return ref.getBlocks(request);
	}
	
	@Override
	public AABBI getAABB() {
		return ref.getAABB();
	}
	
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder {
		
		private UUID uuid;
		private BlockProvider ref;
		
		public Builder uuid(UUID uuid) {
			this.uuid = uuid;
			return this;
		}
		
		public Builder ref(BlockProvider ref) {
			this.ref = ref;
			return this;
		}
		
		public ReferenceBlockProvider build() {
			return new ReferenceBlockProvider(uuid, ref);
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
		public BlockProvider load(LoaderSystem loaderSystem) {
			NodeLoader loader = loaderSystem.getLoader(this.ref).orElseThrow(() -> new IllegalStateException("Missing reference: " + this.ref + " in " + getUuid()));
			BlockProvider b = loader.load(loaderSystem, BlockProvider.class).orElseThrow(wrongType(REF));
			return new ReferenceBlockProvider(getUuid(), b);
		}
		
	}
	
}
