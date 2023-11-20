package com.ferreusveritas.block.provider;

import com.ferreusveritas.api.Request;
import com.ferreusveritas.block.Block;
import com.ferreusveritas.block.Blocks;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.support.json.InvalidJsonProperty;
import com.ferreusveritas.support.json.JsonObj;

import java.util.Optional;

/**
 * A BlockProvider that provides a single block type for every position in the world.
 */
public class SolidBlockProvider extends BlockProvider {
	
	public static final String TYPE = "solid";
	
	private final Block block;
	
	public SolidBlockProvider(Block block) {
		this.block = block;
	}
	
	public SolidBlockProvider(JsonObj src) {
		super(src);
		this.block = src.getObj("block").map(Block::new).orElseThrow(() -> new InvalidJsonProperty("Missing block"));
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public Optional<Blocks> getBlocks(Request request) {
		return Optional.of(new Blocks(request.area(), block));
	}
	
	@Override
	public boolean intersects(AABBI area) {
		return true;
	}
	
	@Override
	public Optional<AABBI> getAABB() {
		return Optional.of(AABBI.INFINITE);
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set("block", block);
	}
	
}
