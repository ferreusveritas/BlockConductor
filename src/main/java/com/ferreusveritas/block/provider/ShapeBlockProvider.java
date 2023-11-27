package com.ferreusveritas.block.provider;

import com.ferreusveritas.api.Request;
import com.ferreusveritas.block.Block;
import com.ferreusveritas.block.Blocks;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.shapes.Shape;
import com.ferreusveritas.support.json.JsonObj;

import java.util.Optional;

public class ShapeBlockProvider extends BlockProvider {
	
	public static final String TYPE = "shape";
	public static final String SHAPE = "shape";
	public static final String BLOCK = "block";
	
	private final Shape shape;
	private final Block block;
	
	public ShapeBlockProvider(Scene scene, Shape shape, Block block) {
		super(scene);
		this.shape = shape;
		this.block = scene.block(block);
		if(shape == null) {
			throw new IllegalArgumentException("shape cannot be null");
		}
		if(block == null) {
			throw new IllegalArgumentException("block cannot be null");
		}
	}
	
	public ShapeBlockProvider(Scene scene, JsonObj src) {
		super(scene, src);
		this.shape = src.getObj(SHAPE).map(scene::createShape).orElseThrow(missing(SHAPE));
		this.block = src.getObj(BLOCK).map(scene::block).orElseThrow(missing(BLOCK));
	}
	
	@Override
	public String getType() {
		return TYPE;
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
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(SHAPE, shape)
			.set(BLOCK, block);
	}
	
}
