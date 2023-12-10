package com.ferreusveritas.block.provider;

import com.ferreusveritas.api.Request;
import com.ferreusveritas.block.Blocks;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.InvalidJsonProperty;
import com.ferreusveritas.support.json.JsonObj;

import java.util.Optional;
import java.util.UUID;

/**
 * A block provider that references another block provider by its UUID.
 */
public class ReferenceBlockProvider extends BlockProvider {
	
	public static final String TYPE = "reference";
	
	private final BlockProvider ref;
	
	public ReferenceBlockProvider(Scene scene, BlockProvider ref) {
		super(scene);
		this.ref = ref;
	}
	
	public ReferenceBlockProvider(Scene scene, JsonObj src) {
		super(scene, src);
		UUID uuid = src.getString("ref").map(UUID::fromString).orElseThrow(() -> new InvalidJsonProperty("ref missing"));
		this.ref = scene.getBlockProvider(uuid);
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public Optional<Blocks> getBlocks(Request request) {
		return ref.getBlocks(request);
	}
	
	@Override
	public AABBI getAABB() {
		return ref.getAABB();
	}
	
}
