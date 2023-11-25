package com.ferreusveritas.controllers;

import com.ferreusveritas.api.Request;
import com.ferreusveritas.block.Blocks;
import com.ferreusveritas.scene.MainScene;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.nbt.NbtHelper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("blocks")
public class BlocksController {
	
	private final Scene scene = MainScene.getScene();
	
	@PostMapping(path="", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Blocks getBlocks(@RequestBody Request request) {
		return scene.getRoot().getBlocks(request).orElse(null);
	}
	
	@PostMapping(path="/nbt", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public @ResponseBody byte[] getBlocksNBT(@RequestBody Request request) {
		return NbtHelper.serialize(scene.getRoot().getBlocks(request).orElse(null));
	}
	
}