package com.ferreusveritas.scene;

import com.ferreusveritas.api.AABB;
import com.ferreusveritas.api.BlockTypes;
import com.ferreusveritas.api.VecI;
import com.ferreusveritas.blockproviders.BlockProvider;
import com.ferreusveritas.blockproviders.CombineBlockProvider;
import com.ferreusveritas.blockproviders.ShapeBlockProvider;
import com.ferreusveritas.shapes.*;

public class MainScene {
	
	private static final BlockProvider provider = createProvider();
	
	private static BlockProvider createProvider() {
		VecI move = new VecI(4, 3, 4);
		
		Shape sphere = new SphereShape(new VecI(8, 64, 8), 12);
		Shape transSphere = new TranslateShape(sphere, move);
		CavitateShape cavitate = new CavitateShape(transSphere);
		BoxShape boxShape = new BoxShape(new AABB(4, 1, 4, 8, 84, 8));
		Shape surface = new SurfaceShape(VecI.EAST, 5);
		
		BlockProvider sphereProvider = new ShapeBlockProvider(cavitate, BlockTypes.STONE);
		BlockProvider cuboidProvider = new ShapeBlockProvider(boxShape, BlockTypes.DIRT);
		BlockProvider surfaceProvider = new ShapeBlockProvider(new DifferenceShape(surface, transSphere), BlockTypes.SANDSTONE);
		
		return new CombineBlockProvider.Builder()
			.add(cuboidProvider)
			.add(surfaceProvider)
			.add(sphereProvider)
			.build();
	}
	
	public static BlockProvider getProvider() {
		return provider;
	}
	
}
