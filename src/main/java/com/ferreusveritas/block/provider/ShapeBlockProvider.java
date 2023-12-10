package com.ferreusveritas.block.provider;

import com.ferreusveritas.api.Request;
import com.ferreusveritas.block.Block;
import com.ferreusveritas.block.Blocks;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.shape.Shape;
import com.ferreusveritas.support.json.JsonObj;

import java.util.Optional;

public class ShapeBlockProvider extends BlockProvider {
	
	public static final String TYPE = "shape";
	public static final String BLOCK = "block";
	public static final String SHAPE = "shape";
	
	private final Block block;
	private final Shape shape;
	
	public ShapeBlockProvider(Scene scene, Shape shape, Block block) {
		super(scene);
		this.block = scene.block(block);
		this.shape = shape;
		if(block == null) {
			throw new IllegalArgumentException("block cannot be null");
		}
		if(shape == null) {
			throw new IllegalArgumentException("shape cannot be null");
		}
	}
	
	public ShapeBlockProvider(Scene scene, JsonObj src) {
		super(scene, src);
		this.block = src.getObj(BLOCK).map(scene::block).orElseThrow(missing(BLOCK));
		this.shape = src.getObj(SHAPE).map(scene::createShape).orElseThrow(missing(SHAPE));
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
		AABBI bounds = intersect(area);
		if(bounds == AABBI.EMPTY) {
			return Optional.empty();
		}
		Blocks blocks = new Blocks(area.size());
		bounds.forEach((abs,rel) -> processBlock(blocks, abs, abs.sub(area.min())));
		return Optional.of(blocks);
	}
	
	private void processBlock(Blocks blocks, Vec3I absPos, Vec3I relPos) {
		if(shape.isInside(absPos)) {
			blocks.set(relPos, block);
		}
	}
	
	@Override
	public AABBI getAABB() {
		return shape.bounds().toAABBI();
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(BLOCK, block)
			.set(SHAPE, shape);
	}
	
}
