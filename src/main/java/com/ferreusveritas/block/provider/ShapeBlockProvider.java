package com.ferreusveritas.block.provider;

import com.ferreusveritas.api.*;
import com.ferreusveritas.block.Block;
import com.ferreusveritas.block.Blocks;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.shapes.Shape;

import java.util.Optional;

public class ShapeBlockProvider extends BlockProvider {
	
	private final Shape shapeProvider;
	private final Block block;

	public ShapeBlockProvider(Shape shapeProvider, Block block) {
		this.shapeProvider = shapeProvider;
		this.block = block;
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
		if(shapeProvider.isInside(absPos)) {
			blocks.set(relPos, block);
		}
	}
	
	@Override
	public Optional<AABBI> getAABB() {
		return shapeProvider.getAABB();
	}
	
}
