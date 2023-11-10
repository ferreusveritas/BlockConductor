package com.ferreusveritas.block.provider;

import com.ferreusveritas.api.*;
import com.ferreusveritas.block.Block;
import com.ferreusveritas.block.Blocks;
import com.ferreusveritas.math.AABB;
import com.ferreusveritas.math.VecI;
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
		AABB area = request.area();
		AABB bounds = intersect(area).orElse(null);
		if(bounds == null) {
			return Optional.empty();
		}
		Blocks blocks = new Blocks(area);
		bounds.forEach((abs,rel) -> processBlock(blocks, abs, abs.sub(area.min())));
		return Optional.of(blocks);
	}
	
	private void processBlock(Blocks blocks, VecI absPos, VecI relPos) {
		if(shapeProvider.isInside(absPos)) {
			blocks.set(relPos, block);
		}
	}
	
	@Override
	public Optional<AABB> getAABB() {
		return shapeProvider.getAABB();
	}
	
}
