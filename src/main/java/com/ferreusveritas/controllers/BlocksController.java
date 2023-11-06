package com.ferreusveritas.controllers;

import com.ferreusveritas.api.AABB;
import com.ferreusveritas.api.BlockTypes;
import com.ferreusveritas.api.Blocks;
import com.ferreusveritas.api.VecI;
import com.ferreusveritas.blockproviders.*;
import com.ferreusveritas.shapeproviders.BoxShapeProvider;
import com.ferreusveritas.shapeproviders.SphereShapeProvider;
import com.ferreusveritas.shapeproviders.SurfaceProvider;
import com.ferreusveritas.support.NbtHelper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("blocks")
public class BlocksController {

	private final BlockProvider solidProvider = new SolidBlockProvider(BlockTypes.AIR);
	private final BlockProvider sphereProvider = new ShapeBlockProvider(new SphereShapeProvider(new VecI(8, 64, 8), 12), BlockTypes.STONE);
	private final BlockProvider cuboidProvider = new ShapeBlockProvider(new BoxShapeProvider(new AABB(4, 1, 4, 8, 84, 8)), BlockTypes.DIRT);
	private final BlockProvider surfaceProvider = new ShapeBlockProvider(new SurfaceProvider(VecI.EAST, 5), BlockTypes.SANDSTONE);
	private final BlockProvider translateProvider = new TranslateProvider(sphereProvider, new VecI(4, 3, 4));
	
	private final CombineBlockProvider combineProvider = new CombineBlockProvider.Builder()
		.add(translateProvider)
		.add(cuboidProvider)
		.add(surfaceProvider)
		.build();
	
	@PostMapping(path="", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Blocks getBlocks(@RequestBody AABB aabb) {
		return combineProvider.getBlocks(aabb).orElse(null);
	}
	
	@PostMapping(path="/nbt", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public @ResponseBody byte[] getBlocksNBT(@RequestBody AABB aabb) {
		return NbtHelper.serialize(combineProvider.getBlocks(aabb).orElse(null));
	}
	
}