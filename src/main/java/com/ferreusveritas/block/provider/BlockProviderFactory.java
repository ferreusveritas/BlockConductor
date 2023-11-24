package com.ferreusveritas.block.provider;

import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.BiFactory;
import com.ferreusveritas.support.json.JsonObj;

public class BlockProviderFactory {
	
	private static final BiFactory<BlockProvider> FACTORY = new BiFactory.Builder<BlockProvider>()
		.add(CombineBlockProvider.TYPE, CombineBlockProvider::new)
		.add(MapperBlockProvider.TYPE, MapperBlockProvider::new)
		.add(RoutingBlockProvider.TYPE, RoutingBlockProvider::new)
		.add(ShapeBlockProvider.TYPE, ShapeBlockProvider::new)
		.add(SolidBlockProvider.TYPE, SolidBlockProvider::new)
		.add(TranslateBlockProvider.TYPE, TranslateBlockProvider::new)
		.build();
	
	public static BlockProvider create(Scene scene, JsonObj src) {
		return FACTORY.create(scene, src);
	}
	
	private BlockProviderFactory() {
		throw new IllegalStateException("Utility class");
	}
	
}
