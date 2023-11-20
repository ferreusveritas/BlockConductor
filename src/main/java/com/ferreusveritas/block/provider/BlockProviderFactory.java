package com.ferreusveritas.block.provider;

import com.ferreusveritas.support.Factory;
import com.ferreusveritas.support.json.JsonObj;

public class BlockProviderFactory {
	
	private static final Factory<BlockProvider> factory = new Factory.Builder<BlockProvider>()
		.add(CombineBlockProvider.TYPE, CombineBlockProvider::new)
		.add(MapperBlockProvider.TYPE, MapperBlockProvider::new)
		.add(RoutingBlockProvider.TYPE, RoutingBlockProvider::new)
		.add(ShapeBlockProvider.TYPE, ShapeBlockProvider::new)
		.add(SolidBlockProvider.TYPE, SolidBlockProvider::new)
		.add(TranslateBlockProvider.TYPE, TranslateBlockProvider::new)
		.build();
	
	public static BlockProvider create(JsonObj src) {
		return factory.create(src);
	}
	
	private BlockProviderFactory() {
		throw new IllegalStateException("Utility class");
	}
	
}
