package com.ferreusveritas.node.provider;

import com.ferreusveritas.api.Request;
import com.ferreusveritas.block.Block;
import com.ferreusveritas.block.BlockCache;
import com.ferreusveritas.block.Blocks;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.node.NodeLoader;
import com.ferreusveritas.node.shape.Shape;
import com.ferreusveritas.scene.LoaderSystem;
import com.ferreusveritas.support.json.JsonObj;

import java.util.Optional;
import java.util.UUID;

public class ShapeBlockProvider extends BlockProvider {
	
	public static final String TYPE = "shape";
	public static final String BLOCK = "block";
	public static final String SHAPE = "shape";
	
	private final Block block;
	private final Shape shape;
	private final AABBI aabb;
	
	private ShapeBlockProvider(UUID uuid, Shape shape, Block block) {
		super(uuid);
		this.block = block;
		this.shape = shape;
		if(block == null) {
			throw new IllegalArgumentException("block cannot be null");
		}
		if(shape == null) {
			throw new IllegalArgumentException("shape cannot be null");
		}
		this.aabb = calculateBounds(shape);
	}
	
	private AABBI calculateBounds(Shape shape) {
		return shape.bounds().toAABBI();
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
		return aabb;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(BLOCK, block)
			.set(SHAPE, shape);
	}
	
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder {
		
		private UUID uuid = null;
		private Shape shape = null;
		private Block block = BlockCache.NONE;
		
		public Builder uuid(UUID uuid) {
			this.uuid = uuid;
			return this;
		}
		
		public Builder shape(Shape shape) {
			this.shape = shape;
			return this;
		}
		
		public Builder block(Block block) {
			this.block = block;
			return this;
		}
		
		public ShapeBlockProvider build() {
			if(shape == null) {
				throw new IllegalStateException("shape cannot be null");
			}
			return new ShapeBlockProvider(uuid, shape, block);
		}
		
	}
	
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends NodeLoader {
		
		private final Block block;
		private final NodeLoader shape;
		
		public Loader(LoaderSystem loaderSystem, JsonObj src) {
			super(loaderSystem, src);
			this.block = loaderSystem.blockLoader(src, BLOCK);
			this.shape = loaderSystem.loader(src, SHAPE);
		}
		
		@Override
		public BlockProvider load(LoaderSystem loaderSystem) {
			Shape s = shape.load(loaderSystem, Shape.class).orElseThrow(wrongType(SHAPE));
			return new ShapeBlockProvider(getUuid(), s, block);
		}
		
	}
	
}
