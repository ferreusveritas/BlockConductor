package com.ferreusveritas.controllers;

import com.ferreusveritas.api.Request;
import com.ferreusveritas.api.Response;
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
	public @ResponseBody String getBlocks(@RequestBody Request request) {
		Blocks blocks = scene.getRoot().getBlocks(request).orElseThrow();
		Response response = new Response(request.area(), blocks);
		return response.toString();
	}
	
	@PostMapping(path="/nbt", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public @ResponseBody byte[] getBlocksNBT(@RequestBody Request request) {
		Blocks blocks = scene.getRoot().getBlocks(request).orElseThrow();
		Response response = new Response(request.area(), blocks);
		return NbtHelper.serialize(response);
	}
	
}