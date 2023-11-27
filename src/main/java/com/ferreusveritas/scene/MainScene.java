package com.ferreusveritas.scene;

import com.ferreusveritas.block.Block;
import com.ferreusveritas.block.BlockCache;
import com.ferreusveritas.block.provider.*;
import com.ferreusveritas.hunk.Hunk;
import com.ferreusveritas.hunk.SimplexHunk;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.shapes.HeightmapShape;
import com.ferreusveritas.shapes.HunkShape;
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
		BlockProvider noiseTest2 = createNoiseTest2(scene);
		BlockProvider air = new SolidBlockProvider(scene, BlockCache.AIR);
		BlockProvider root = new RoutingBlockProvider(scene, Map.of(
			"", shapeTest,
			"h", heightMapTest,
			"m", modelTest,
			"n", noiseTest,
			"n2", noiseTest2,
			"a", air
		));
		scene.setRoot(root);
		return scene;
	}
	
	private static BlockProvider createNoiseTest(Scene scene) {
		Hunk simplexHunk = new SimplexHunk.Builder().frequency(0.005).build(scene);
		Shape heightMapShape = new HeightmapShape(scene, simplexHunk, 32, new Vec3I(0, 54, 0), false);
		return new ShapeBlockProvider(scene, heightMapShape, new Block("minecraft:stone"));
	}
	
	private static BlockProvider createNoiseTest2(Scene scene) {
		Hunk simplexHunk = new SimplexHunk.Builder().frequency(0.005).build(scene);
		Shape hunkShape = new HunkShape(scene, simplexHunk, 0.99);
		return new ShapeBlockProvider(scene, hunkShape, new Block("minecraft:stone"));
	}
	
	public static Scene getScene() {
		return scene;
	}
	
	private MainScene() {
		throw new IllegalStateException("Utility class");
	}
	
}
