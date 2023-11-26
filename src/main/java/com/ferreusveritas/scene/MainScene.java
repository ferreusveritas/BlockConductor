package com.ferreusveritas.scene;

import com.ferreusveritas.block.Block;
import com.ferreusveritas.block.BlockCache;
import com.ferreusveritas.block.provider.*;
import com.ferreusveritas.image.Image;
import com.ferreusveritas.image.SimplexImage;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.shapes.HeightmapShape;
import com.ferreusveritas.shapes.Shape;
import com.ferreusveritas.support.storage.Storage;

import java.util.Map;

public class MainScene {
	
	private static final Scene scene = createScene();
	
	private static Scene createScene() {
		Scene scene = new Scene();
		BlockProvider shapeTest = BlockProviderFactory.create(scene, Storage.getJson("res://scenes/shapes.json"));
		BlockProvider heightMapTest = BlockProviderFactory.create(scene, Storage.getJson("res://scenes/heightmap.json"));
		BlockProvider modelTest = BlockProviderFactory.create(scene, Storage.getJson("res://scenes/dragon.json"));
		BlockProvider noiseTest = createNoiseTest(scene);
		BlockProvider air = new SolidBlockProvider(scene, BlockCache.AIR);
		BlockProvider root = new RoutingBlockProvider(scene, Map.of(
			"", shapeTest,
			"h", heightMapTest,
			"m", modelTest,
			"n", noiseTest,
			"a", air
		));
		scene.setRoot(root);
		return scene;
	}
	
	private static BlockProvider createNoiseTest(Scene scene) {
		Image noiseImage = new SimplexImage.Builder().frequency(0.005).build(scene);
		Shape heightMapShape = new HeightmapShape(scene, noiseImage, 32, new Vec3I(0, 54, 0), false);
		return new ShapeBlockProvider(scene, heightMapShape, new Block("minecraft:stone"));
	}
	
	public static Scene getScene() {
		return scene;
	}
	
	private MainScene() {
		throw new IllegalStateException("Utility class");
	}
	
}
