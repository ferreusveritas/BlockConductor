package com.ferreusveritas.scene;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ferreusveritas.block.provider.*;
import com.ferreusveritas.image.BufferImage;
import com.ferreusveritas.image.ImageLoader;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.block.BlockTypes;
import com.ferreusveritas.math.Matrix4X4;
import com.ferreusveritas.math.Vec3D;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.model.FullMeshModel;
import com.ferreusveritas.model.ModelLoader;
import com.ferreusveritas.model.QSPModel;
import com.ferreusveritas.model.SimpleMeshModel;
import com.ferreusveritas.shapes.*;

import java.util.Map;

public class MainScene {
	
	private static final ObjectMapper mapper = new ObjectMapper();
	private static final BlockProvider provider = createProvider();
	
	private static BlockProvider createProvider() {
		
		BlockProvider shapeTest = shapeTest();
		BlockProvider heightMapTest = heightMapTest();
		BlockProvider modelTest = modelTest();
		BlockProvider air = air();
		
		return new RoutingBlockProvider(Map.of(
			"", shapeTest,
			"h", heightMapTest,
			"m", modelTest,
			"a", air
		));
	}
	
	private static BlockProvider shapeTest() {
		Vec3I move = new Vec3I(4, 3, 4);
		
		Shape sphere = new SphereShape(new Vec3D(8, 64, 8), 12);
		Shape transSphere = new TranslateShape(sphere, move);
		CavitateShape cavitate = new CavitateShape(transSphere);
		BoxShape boxShape = new BoxShape(new AABBI(4, 1, 4, 8, 84, 8));
		Shape surface = new SurfaceShape(Vec3I.EAST, 15);
		
		BlockProvider sphereProvider = new ShapeBlockProvider(cavitate, BlockTypes.STONE);
		BlockProvider cuboidProvider = new ShapeBlockProvider(boxShape, BlockTypes.DIRT);
		BlockProvider surfaceProvider = new ShapeBlockProvider(new DifferenceShape(surface, transSphere), BlockTypes.SANDSTONE);
		
		return new CombineBlockProvider(cuboidProvider, surfaceProvider, sphereProvider);
	}
	
	private static BlockProvider heightMapTest() {
		BufferImage image = ImageLoader.load("res://skull.png");
		Shape heightMap = new HeightmapShape(image, 16, Vec3I.ZERO.up(56), true);
		return new ShapeBlockProvider(heightMap, BlockTypes.STONE);
	}
	
	private static BlockProvider modelTest() {
		FullMeshModel model = ModelLoader.load("res://dragon_skull.obj").orElseThrow();
		SimpleMeshModel simpleModel = model.toSimpleMeshModel();
		QSPModel qsp = simpleModel.calculateQSP();
		Shape modelShape = new MeshModelShape(qsp, Matrix4X4.IDENTITY
				.scale(new Vec3D(.25, .25, .25))
				.rotateX(Math.toRadians(12))
				.rotateY(Math.toRadians(45))
		);
		Shape transModelShape = new TranslateShape(modelShape, new Vec3I(15, 56, 16));
		Shape cylinderShape = new CylinderShape(new Vec3D(16, 56, 16), 15.5, 1);
		
		BlockProvider skullBlocks = new ShapeBlockProvider(transModelShape, BlockTypes.BONE);
		BlockProvider cylinderBlocks = new ShapeBlockProvider(cylinderShape, BlockTypes.BLACKSTONE);
		BlockProvider skullAndCylinder = new CombineBlockProvider(skullBlocks, cylinderBlocks);
		
		return skullAndCylinder;
	}
	
	private static BlockProvider air() {
		return new SolidBlockProvider(BlockTypes.AIR);
	}
	
	public static BlockProvider getProvider() {
		return provider;
	}
	
	private MainScene() {
		throw new IllegalStateException("Utility class");
	}
	
}
