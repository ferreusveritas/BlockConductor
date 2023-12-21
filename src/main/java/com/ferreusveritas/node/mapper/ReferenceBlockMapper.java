package com.ferreusveritas.node.mapper;

import com.ferreusveritas.block.Block;
import com.ferreusveritas.node.NodeLoader;
import com.ferreusveritas.scene.LoaderSystem;
import com.ferreusveritas.support.json.JsonObj;

import java.util.UUID;

public class ReferenceBlockMapper extends BlockMapper {
	
	public static final String TYPE = "reference";
	public static final String REF = "ref";
	
	private final BlockMapper ref;
	
	private ReferenceBlockMapper(UUID uuid, BlockMapper ref) {
		super(uuid);
		this.ref = ref;
	}
	
	@Override
	public Block map(Block block) {
		return ref.map(block);
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
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder {
		
		private UUID uuid = null;
		private BlockMapper ref = IdentityBlockMapper.IDENTITY;
		
		public Builder uuid(UUID uuid) {
			this.uuid = uuid;
			return this;
		}
		
		public Builder ref(BlockMapper ref) {
			this.ref = ref;
			return this;
		}
		
		public ReferenceBlockMapper build() {
			return new ReferenceBlockMapper(uuid, ref);
		}
		
	}
	
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends NodeLoader {
		
		private final UUID ref;
		
		public Loader(LoaderSystem loaderSystem, UUID uuid, JsonObj src) {
			super(uuid);
			this.ref = src.getString(REF).map(UUID::fromString).orElseThrow(() -> new IllegalArgumentException("ReferenceBlockMapper requires a ref"));
		}
		
		@Override
		public BlockMapper load(LoaderSystem loaderSystem) {
			NodeLoader loader = loaderSystem.getLoader(ref).orElseThrow(() -> new IllegalArgumentException("ReferenceBlockMapper requires a valid ref"));
			BlockMapper mapper = loader.load(loaderSystem, BlockMapper.class).orElseThrow(wrongType(REF));
			return new ReferenceBlockMapper(getUuid(), mapper);
		}
		
	}
	
}
