package com.ferreusveritas.controllers;

import com.ferreusveritas.api.*;
import com.ferreusveritas.block.Blocks;
import com.ferreusveritas.block.provider.BlockProvider;
import com.ferreusveritas.blockproviders.*;
import com.ferreusveritas.scene.MainScene;
import com.ferreusveritas.support.NbtHelper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("blocks")
public class BlocksController {
	
	private final BlockProvider blockProvider = MainScene.getProvider();
	
	@PostMapping(path="", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Blocks getBlocks(@RequestBody Request request) {
		return blockProvider.getBlocks(request).orElse(null);
	}
	
	@PostMapping(path="/nbt", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public @ResponseBody byte[] getBlocksNBT(@RequestBody Request request) {
		return NbtHelper.serialize(blockProvider.getBlocks(request).orElse(null));
	}
	
}