package com.ferreusveritas.block.mapper;

import com.ferreusveritas.support.Factory;
import com.ferreusveritas.support.json.JsonObj;

public class BlockMapperFactory {
	
	private static final Factory<BlockMapper> factory = new Factory.Builder<BlockMapper>()
		.add(IdentityBlockMapper.TYPE, IdentityBlockMapper::new)
		.add(SimpleBlockMapper.TYPE, SimpleBlockMapper::new)
		.build();
	
	public static BlockMapper create(JsonObj src) {
		return factory.create(src);
	}
	
	private BlockMapperFactory() {
		throw new IllegalStateException("Utility class");
	}
	
}
