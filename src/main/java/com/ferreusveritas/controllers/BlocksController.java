package com.ferreusveritas.controllers;

import com.ferreusveritas.api.Request;
import com.ferreusveritas.api.Response;
import com.ferreusveritas.create.CreateSchematicConvertor;
import com.ferreusveritas.scene.BlockSystem;
import com.ferreusveritas.support.nbt.NbtHelper;
import net.querz.nbt.tag.CompoundTag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("blocks")
public class BlocksController {
	
	BlockSystem blockSystem = new BlockSystem();
	
	@PostMapping(path="", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getBlocks(@RequestBody Request request) {
		Response response = blockSystem.process(request);
		return response.toString();
	}
	
	@PostMapping(path="/nbt", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public byte[] getBlocksNBT(@RequestBody Request request) {
		Response response = blockSystem.process(request);
		return NbtHelper.serialize(response);
	}
	
	@PostMapping(path="/schematic", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public byte[] getBlocksSchematic(@RequestBody Request request) {
		Response response = blockSystem.process(request);
		CreateSchematicConvertor convertor = new CreateSchematicConvertor();
		CompoundTag nbt = convertor.convert(response);
		return NbtHelper.serialize(nbt);
	}
	
}