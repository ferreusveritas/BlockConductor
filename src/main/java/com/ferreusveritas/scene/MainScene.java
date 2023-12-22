package com.ferreusveritas.scene;

import com.ferreusveritas.node.provider.BlockProvider;
import com.ferreusveritas.node.provider.RoutingBlockProvider;
import com.ferreusveritas.support.storage.Storage;

import java.util.Map;

public class MainScene {
	
	private static final Scene scene = createScene();
	
	private static BlockProvider quickScene(String sceneName) {
		String resName = "res://scenes/" + sceneName + ".json";
		return new Scene(Storage.getJson(resName)).getRoot(BlockProvider.class).orElseThrow();
	}
	
	private static Scene createScene() {
		Scene scene = new Scene();
		BlockProvider root = new RoutingBlockProvider.Builder().add(Map.of(
			"a", quickScene("air"),
			"", quickScene("shapes"),
			"c", quickScene("cylinder"),
			"h", quickScene("heightmap"),
			"m", quickScene("model"),
			"n", quickScene("noise1"),
			"n2", quickScene("noise2"),
			"p", quickScene("pyramid"),
			"s", quickScene("shyguy")
		)).build();
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
