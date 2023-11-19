package com.ferreusveritas.block.provider;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.ferreusveritas.api.Request;
import com.ferreusveritas.block.Block;
import com.ferreusveritas.block.Blocks;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.shapes.Shape;

import java.util.Map;
import java.util.Optional;

public class ShapeBlockProvider extends BlockProvider {
	
	public static final String TYPE = "shape";
	
	private final Shape shape;
	private final Block block;
	
	@JsonCreator
	public ShapeBlockProvider(
		@JsonProperty("shape") Shape shape,
		@JsonProperty("block") Block block
	) {
		this.shape = shape;
		this.block = block;
		if(shape == null) {
			throw new IllegalArgumentException("shape cannot be null");
		}
		if(block == null) {
			throw new IllegalArgumentException("block cannot be null");
		}
	}
	
	@JsonValue
	private Map<String, Object> getJson() {
		return Map.of(
			"type", TYPE,
			"shape", shape,
			"block", block
		);
	}
	
	public Block getBlock() {
		return block;
	}
	
	@Override
	public Optional<Blocks> getBlocks(Request request) {
		AABBI area = request.area();
		AABBI bounds = intersect(area).orElse(null);
		if(bounds == null) {
			return Optional.empty();
		}
		Blocks blocks = new Blocks(area);
		bounds.forEach((abs,rel) -> processBlock(blocks, abs, abs.sub(area.min())));
		return Optional.of(blocks);
	}
	
	private void processBlock(Blocks blocks, Vec3I absPos, Vec3I relPos) {
		if(shape.isInside(absPos)) {
			blocks.set(relPos, block);
		}
	}
	
	@Override
	public Optional<AABBI> getAABB() {
		return shape.getAABB();
	}
	
}
