package com.ferreusveritas.scene;

import com.ferreusveritas.block.BlockTypes;
import com.ferreusveritas.block.provider.BlockProvider;
import com.ferreusveritas.block.provider.BlockProviderFactory;
import com.ferreusveritas.block.provider.RoutingBlockProvider;
import com.ferreusveritas.block.provider.SolidBlockProvider;
import com.ferreusveritas.support.json.JsonObj;
import com.ferreusveritas.support.storage.Storage;

import java.util.Map;

public class MainScene {
	
	private static final BlockProvider provider = createProvider();
	
	private static BlockProvider createProvider() {
		
		Scene scene = new Scene();
		
		BlockProvider shapeTest = shapeTest(scene);
		BlockProvider heightMapTest = heightMapTest(scene);
		BlockProvider modelTest = modelTest(scene);
		BlockProvider air = air(scene);
		
		return new RoutingBlockProvider(scene, Map.of(
			"", shapeTest,
			"h", heightMapTest,
			"m", modelTest,
			"a", air
		));
	}
	
	private static BlockProvider shapeTest(Scene scene) {
		JsonObj shapes = Storage.getJson("res://scenes/shapes.json");
		return BlockProviderFactory.create(scene, shapes);
		
	}
	
	private static BlockProvider heightMapTest(Scene scene) {
		JsonObj shapes = Storage.getJson("res://scenes/heightmap.json");
		return BlockProviderFactory.create(scene, shapes);
	}
	
	private static BlockProvider modelTest(Scene scene) {
		JsonObj dragon = Storage.getJson("res://scenes/dragon.json");
		return BlockProviderFactory.create(scene, dragon);
	}
	
	private static BlockProvider air(Scene scene) {
		return new SolidBlockProvider(scene, BlockTypes.AIR);
	}
	
	public static BlockProvider getProvider() {
		return provider;
	}
	
	private MainScene() {
		throw new IllegalStateException("Utility class");
	}
	
}
