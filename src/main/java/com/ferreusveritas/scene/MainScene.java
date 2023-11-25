package com.ferreusveritas.scene;

import com.ferreusveritas.block.BlockTypes;
import com.ferreusveritas.block.provider.BlockProvider;
import com.ferreusveritas.block.provider.BlockProviderFactory;
import com.ferreusveritas.block.provider.RoutingBlockProvider;
import com.ferreusveritas.block.provider.SolidBlockProvider;
import com.ferreusveritas.support.storage.Storage;

import java.util.Map;

public class MainScene {
	
	private static final Scene scene = createScene();
	
	private static Scene createScene() {
		Scene scene = new Scene();
		BlockProvider shapeTest = BlockProviderFactory.create(scene, Storage.getJson("res://scenes/shapes.json"));
		BlockProvider heightMapTest = BlockProviderFactory.create(scene, Storage.getJson("res://scenes/heightmap.json"));
		BlockProvider modelTest = BlockProviderFactory.create(scene, Storage.getJson("res://scenes/dragon.json"));
		BlockProvider air = new SolidBlockProvider(scene, BlockTypes.AIR);
		BlockProvider root = new RoutingBlockProvider(scene, Map.of(
			"", shapeTest,
			"h", heightMapTest,
			"m", modelTest,
			"a", air
		));
		scene.setRoot(root);
		return scene;
	}
	
	public static Scene getScene() {
		return scene;
	}
	
	private MainScene() {
		throw new IllegalStateException("Utility class");
	}
	
}
