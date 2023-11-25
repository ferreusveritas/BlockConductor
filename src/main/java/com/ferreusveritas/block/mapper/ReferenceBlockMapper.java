package com.ferreusveritas.block.mapper;

import com.ferreusveritas.block.Block;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

import java.util.UUID;

public class ReferenceBlockMapper extends BlockMapper {
	
	public static final String TYPE = "reference";
	
	private final BlockMapper ref;
	
	public ReferenceBlockMapper(Scene scene, BlockMapper ref) {
		super(scene);
		this.ref = ref;
	}
	
	public ReferenceBlockMapper(Scene scene, JsonObj src) {
		super(scene, src);
		UUID uuid = src.getString("ref").map(UUID::fromString).orElse(null);
		this.ref = scene.getBlockMapper(uuid);
	}
	
	@Override
	public Block map(Block block) {
		return ref.map(block);
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
}
