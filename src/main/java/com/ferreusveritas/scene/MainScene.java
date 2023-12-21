package com.ferreusveritas.scene;

import com.ferreusveritas.block.Block;
import com.ferreusveritas.block.BlockCache;
import com.ferreusveritas.node.model.MeshModel;
import com.ferreusveritas.node.provider.*;
import com.ferreusveritas.node.shape.*;
import com.ferreusveritas.node.shape.support.CombineOperation;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.support.storage.Storage;
import com.ferreusveritas.node.transform.RotateY;
import com.ferreusveritas.node.transform.Scale;
import com.ferreusveritas.node.transform.Transforms;
import com.ferreusveritas.node.transform.Translate;

import java.util.Map;

public class MainScene {
	
	private static final Scene scene = createScene();
	public static final String SHYGUY_OBJ = "res://models/shyguy.obj";
	
	private static Scene createScene() {
		Scene scene = new Scene();
		BlockProvider air = new SolidBlockProvider(BlockCache.AIR);
		BlockProvider shapeTest = new Scene(Storage.getJson("res://scenes/shapes.json")).getRoot(BlockProvider.class).orElseThrow();
		BlockProvider cylinderTest = new Scene(Storage.getJson("res://scenes/cylinder.json")).getRoot(BlockProvider.class).orElseThrow();
		BlockProvider heightMapTest = new Scene(Storage.getJson("res://scenes/heightmap.json")).getRoot(BlockProvider.class).orElseThrow();
		BlockProvider modelTest = new Scene(Storage.getJson("res://scenes/dragon.json")).getRoot(BlockProvider.class).orElseThrow();
		BlockProvider noiseTest = createNoiseTest();
		BlockProvider noiseTest2 = createNoiseTest2();
		BlockProvider pyramidTest = createPyramidTest();
		BlockProvider shyGuyTest = createShyGuyTest();
		
		BlockProvider root = new RoutingBlockProvider.Builder().add(Map.of(
			"a", air,
			"", shapeTest,
			"c", cylinderTest,
			"h", heightMapTest,
			"m", modelTest,
			"n", noiseTest,
			"n2", noiseTest2,
			"p", pyramidTest,
			"s", shyGuyTest
		)).build();
		scene.setRoot(root);
		return scene;
	}
	
	private static BlockProvider createNoiseTest() {
		Shape simplexShape = new SimplexShape.Builder().frequency(0.005).build();
		Shape heightMapShape = new HeightMapShape.Builder().shape(simplexShape).height(32.0).build();
		Shape translateShape = new TranslateShape.Builder().shape(heightMapShape).offset(new Vec3I(0, 56, 0)).build();
		return new ShapeBlockProvider.Builder().shape(translateShape).block(new Block("minecraft:stone")).build();
	}
	
	private static BlockProvider createNoiseTest2() {
		Shape simplexShape = new SimplexShape.Builder().frequency(0.005).build();
		Shape gradientShape = new GradientShape.Builder().minY(0.0).maxY(64.0).build();
		Shape multiplyShape = new CombineShape.Builder().operation(CombineOperation.MUL).add(simplexShape, gradientShape).build();
		Shape translateShape = new TranslateShape.Builder().shape(multiplyShape).offset(new Vec3I(0, 56, 0)).build();
		return new TerrainBlockProvider.Builder()
			.shape(translateShape)
			.fill(new Block("minecraft:stone"))
			.surface(new Block("minecraft:grass_block"))
			.subSurface(new Block("minecraft:dirt"))
			.build();
	}
	
	private static BlockProvider createPyramidTest() {
		Shape pyramidShape = new PyramidShape.Builder().height(16.0).base(32.0).build();
		Shape translateShape = new TransformShape.Builder().shape(pyramidShape).transform(
			new Transforms.Builder().add(
				//new RotateX(25.0),
				//new RotateZ(45.0),
				new Translate.Builder().offset(new Vec3D(20.5, 56, 20.5)).build()
			).build()
		).build();
		return new ShapeBlockProvider.Builder()
			.shape(translateShape)
			.block(new Block("minecraft:sandstone"))
			.build();
	}
	
	public static BlockProvider createShyGuyTest() {
		BlockProvider body = loadShyPart("body", "red_concrete");
		BlockProvider face = loadShyPart("face", "black_concrete");
		BlockProvider strap = loadShyPart("strap", "brown_concrete");
		BlockProvider mask = loadShyPart("mask", "white_concrete");
		BlockProvider shoes = loadShyPart("shoes", "blue_concrete");
		BlockProvider soles = loadShyPart("soles", "brown_concrete");
		BlockProvider belt = loadShyPart("belt", "black_concrete");
		BlockProvider buckle = loadShyPart("buckle", "yellow_concrete");
		
		return new CombineBlockProvider.Builder().add(body, face, strap, mask, shoes, soles, belt, buckle).build();
	}
	
	private static BlockProvider loadShyPart(String part, String material) {
		MeshModel model = new MeshModel.Builder().resource(SHYGUY_OBJ).part(part).build();
		Shape modelShape = new ModelShape.Builder().model(model).build();
		Shape translateShape = new TransformShape.Builder().shape(modelShape).transform(
			new Transforms.Builder().add(
				new RotateY.Builder().degrees(-45.0).build(),
				new Scale.Builder().size(1.25).build(),
				new Translate.Builder().offset(new Vec3D(32.0, 56, 32.0)).build()
			).build()
		).build();
		return new ShapeBlockProvider.Builder().shape(translateShape).block(new Block("minecraft:" + material)).build();
	}
	
	
	public static Scene getScene() {
		return scene;
	}
	
	private MainScene() {
		throw new IllegalStateException("Utility class");
	}
	
}
