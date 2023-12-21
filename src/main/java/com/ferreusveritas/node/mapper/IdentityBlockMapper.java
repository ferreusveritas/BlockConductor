package com.ferreusveritas.node.mapper;

import com.ferreusveritas.block.Block;
import com.ferreusveritas.node.NodeLoader;
import com.ferreusveritas.scene.LoaderSystem;
import com.ferreusveritas.support.json.JsonObj;

import java.util.UUID;

/**
 * A BlockMapper that does nothing.
 * Blocks in will match blocks out.
 */
public class IdentityBlockMapper extends BlockMapper {
	
	public static final String TYPE = "identity";
	
	private IdentityBlockMapper(UUID uuid) {
		super(uuid);
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public Block map(Block block) {
		return block;
	}
	
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends NodeLoader {
		
		public Loader(LoaderSystem loaderSystem, JsonObj src) {
			super(loaderSystem, src);
		}
		
		@Override
		public BlockMapper load(LoaderSystem loaderSystem) {
			return new IdentityBlockMapper(getUuid());
		}
		
	}
	
}
