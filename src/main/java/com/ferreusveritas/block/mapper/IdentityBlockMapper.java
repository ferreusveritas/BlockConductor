package com.ferreusveritas.block.mapper;

import com.ferreusveritas.block.Block;
import com.ferreusveritas.support.json.JsonObj;

/**
 * A BlockMapper that does nothing.
 * Blocks in will match blocks out.
 */
public class IdentityBlockMapper extends BlockMapper {
	
	public static final String TYPE = "identity";
	
	public IdentityBlockMapper() {
		super();
	}
	
	public IdentityBlockMapper(JsonObj src) {
		super(src);
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public Block map(Block block) {
		return block;
	}
	
}
