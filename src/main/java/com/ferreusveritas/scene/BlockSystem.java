package com.ferreusveritas.scene;

import com.ferreusveritas.api.Request;
import com.ferreusveritas.api.Response;
import com.ferreusveritas.block.Blocks;
import com.ferreusveritas.node.ports.OutputPort;
import com.ferreusveritas.node.provider.BlockProvider;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.support.storage.Storage;

public class BlockSystem {
	
	public static final Response EMPTY_RESPONSE = new Response(null, null);
	
	private final Scene scene = Storage.getObject("res://scenes/main.json", Scene.class);
	
	public Response process(Request request) {
		BlockProvider blockProvider = scene.root().getOutputPort("blocks", BlockProvider.class).map(OutputPort::read).orElseThrow();
		Blocks blocks = blockProvider.getBlocks(request).orElse(null);
		Response response = new Response(request.area(), blocks);
		response = optimize(response);
		return response;
	}
	
	private Response optimize(Response response) {
		if(response.getBlocks().isEmpty()) {
			return EMPTY_RESPONSE;
		}
		Blocks blocks = response.blocks();
		AABBI used = blocks.getActive().orElse(null);
		if(used == null) {
			return EMPTY_RESPONSE;
		}
		AABBI newArea = used.offset(response.area().min());
		blocks = blocks.crop(used).orElse(null);
		if(blocks == null) {
			return EMPTY_RESPONSE;
		}
		return new Response(newArea, blocks);
	}
	
}
