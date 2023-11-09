package com.ferreusveritas.controllers;

import com.ferreusveritas.api.AABB;
import com.ferreusveritas.api.BlockTypes;
import com.ferreusveritas.api.Blocks;
import com.ferreusveritas.api.VecI;
import com.ferreusveritas.blockproviders.*;
import com.ferreusveritas.scene.MainScene;
import com.ferreusveritas.shapes.BoxShape;
import com.ferreusveritas.shapes.CavitateShape;
import com.ferreusveritas.shapes.SphereShape;
import com.ferreusveritas.shapes.SurfaceShape;
import com.ferreusveritas.support.NbtHelper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("blocks")
public class BlocksController {
	
	private final BlockProvider blockProvider = MainScene.getProvider();
	
	@PostMapping(path="", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Blocks getBlocks(@RequestBody AABB aabb) {
		return blockProvider.getBlocks(aabb).orElse(null);
	}
	
	@PostMapping(path="/nbt", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public @ResponseBody byte[] getBlocksNBT(@RequestBody AABB aabb) {
		return NbtHelper.serialize(blockProvider.getBlocks(aabb).orElse(null));
	}
	
}