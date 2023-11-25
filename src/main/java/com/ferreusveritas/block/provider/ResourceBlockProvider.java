package com.ferreusveritas.block.provider;

import com.ferreusveritas.api.Request;
import com.ferreusveritas.block.Blocks;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.InvalidJsonProperty;
import com.ferreusveritas.support.json.JsonObj;
import com.ferreusveritas.support.storage.Storage;

import java.util.Optional;

public class ResourceBlockProvider extends BlockProvider {
	
	public static final String TYPE = "resource";
	
	private final String path;
	private final BlockProvider provider;
	
	public ResourceBlockProvider(Scene scene, String path) {
		super(scene);
		this.path = path;
		this.provider = scene.createBlockProvider(Storage.getJson(path));
	}
	
	public ResourceBlockProvider(Scene scene, JsonObj src) {
		super(scene, src);
		this.path = src.getString("path").orElseThrow(() -> new InvalidJsonProperty("Missing path"));
		this.provider = scene.createBlockProvider(Storage.getJson(path));
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public Optional<Blocks> getBlocks(Request request) {
		return provider.getBlocks(request);
	}
	
	@Override
	public Optional<AABBI> getAABB() {
		return provider.getAABB();
	}
	
}
