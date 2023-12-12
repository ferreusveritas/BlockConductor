package com.ferreusveritas.scene;

import com.ferreusveritas.block.Block;
import com.ferreusveritas.block.BlockCache;
import com.ferreusveritas.block.provider.*;
import com.ferreusveritas.model.MeshModel;
import com.ferreusveritas.shape.*;
import com.ferreusveritas.shape.support.CombineOperation;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.support.storage.Storage;
import com.ferreusveritas.transform.RotateY;
import com.ferreusveritas.transform.Scale;
import com.ferreusveritas.transform.Transforms;
import com.ferreusveritas.transform.Translate;

import java.util.Map;

public class MainScene {
	
	private static final Scene scene = createScene();
	
	private static Scene createScene() {
		Scene scene = new Scene();
		BlockProvider air = new SolidBlockProvider(scene, BlockCache.AIR);
		BlockProvider shapeTest = BlockProviderFactory.create(scene, Storage.getJson("res://scenes/shapes.json"));
		BlockProvider cylinderTest = BlockProviderFactory.create(scene, Storage.getJson("res://scenes/cylinder.json"));
		BlockProvider heightMapTest = BlockProviderFactory.create(scene, Storage.getJson("res://scenes/heightmap.json"));
		BlockProvider modelTest = BlockProviderFactory.create(scene, Storage.getJson("res://scenes/dragon.json"));
		BlockProvider noiseTest = createNoiseTest(scene);
		BlockProvider noiseTest2 = createNoiseTest2(scene);
		BlockProvider pyramidTest = createPyramidTest(scene);
		BlockProvider shyGuyTest = createShyGuyTest(scene);
		
		BlockProvider root = new RoutingBlockProvider(scene, Map.of(
			"a", air,
			"", shapeTest,
			"c", cylinderTest,
			"h", heightMapTest,
			"m", modelTest,
			"n", noiseTest,
			"n2", noiseTest2,
			"p", pyramidTest,
			"s", shyGuyTest
		));
		scene.setRoot(root);
		return scene;
	}
	
	private static BlockProvider createNoiseTest(Scene scene) {
		Shape simplexShape = new SimplexShape.Builder().frequency(0.005).build(scene);
		Shape heightMapShape = new HeightMapShape(scene, simplexShape, 32.0);
		Shape translateShape = new TranslateShape(scene, heightMapShape, new Vec3I(0, 56, 0));
		return new ShapeBlockProvider(scene, translateShape, new Block("minecraft:stone"));
	}
	
	private static BlockProvider createNoiseTest2(Scene scene) {
		Shape simplexShape = new SimplexShape.Builder().frequency(0.005).build(scene);
		Shape gradientShape = new GradientShape(scene, 0.0, 64.0);
		Shape multiplyShape = new CombineShape(scene, CombineOperation.MUL, simplexShape, gradientShape);
		Shape translateShape = new TranslateShape(scene, multiplyShape, new Vec3I(0, 56, 0));
		return new TerrainBlockProvider(scene, translateShape,
			new Block("minecraft:stone"),
			new Block("minecraft:grass_block"),
			new Block("minecraft:dirt")
		);
	}
	
	private static BlockProvider createPyramidTest(Scene scene) {
		Shape pyramidShape = new PyramidShape(scene, 16.0, 32.0);
		Shape translateShape = new TransformShape(scene, pyramidShape,
			new Transforms(
				//new RotateX(25.0),
				//new RotateZ(45.0),
				new Translate(new Vec3D(20.5, 56, 20.5))
			)
		);
		return new ShapeBlockProvider(scene, translateShape, new Block("minecraft:sandstone"));
	}
	
	public static BlockProvider createShyGuyTest(Scene scene) {
		BlockProvider body = loadShyPart(scene, "body", "red_concrete");
		BlockProvider face = loadShyPart(scene, "face", "black_concrete");
		BlockProvider strap = loadShyPart(scene, "strap", "brown_concrete");
		BlockProvider mask = loadShyPart(scene, "mask", "white_concrete");
		BlockProvider shoes = loadShyPart(scene, "shoes", "blue_concrete");
		BlockProvider soles = loadShyPart(scene, "soles", "brown_concrete");
		BlockProvider belt = loadShyPart(scene, "belt", "black_concrete");
		BlockProvider buckle = loadShyPart(scene, "buckle", "yellow_concrete");
		
		BlockProvider combined = new CombineBlockProvider(scene, body, face, strap, mask, shoes, soles, belt, buckle);
		return combined;
	}
	
	private static BlockProvider loadShyPart(Scene scene, String part, String material) {
		String modelPath = "res://models/shyguy.obj";
		MeshModel model = new MeshModel(scene, modelPath, part);
		Shape modelShape = new ModelShape(scene, model);
		Shape translateShape = new TransformShape(scene, modelShape,
			new Transforms(
				new RotateY(-45.0),
				new Scale(1.25),
				new Translate(new Vec3D(32.0, 56, 32.0))
			)
		);
		return new ShapeBlockProvider(scene, translateShape, new Block("minecraft:" + material));
	}
	
	
	public static Scene getScene() {
		return scene;
	}
	
	private MainScene() {
		throw new IllegalStateException("Utility class");
	}
	
}
