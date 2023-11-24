package com.ferreusveritas.block.mapper;

import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.BiFactory;
import com.ferreusveritas.support.json.JsonObj;

public class BlockMapperFactory {
	
	private static final BiFactory<BlockMapper> FACTORY = new BiFactory.Builder<BlockMapper>()
		.add(IdentityBlockMapper.TYPE, IdentityBlockMapper::new)
		.add(SimpleBlockMapper.TYPE, SimpleBlockMapper::new)
		.build();
	
	public static BlockMapper create(Scene scene, JsonObj src) {
		return FACTORY.create(scene, src);
	}
	
	private BlockMapperFactory() {
		throw new IllegalStateException("Utility class");
	}
	
}
