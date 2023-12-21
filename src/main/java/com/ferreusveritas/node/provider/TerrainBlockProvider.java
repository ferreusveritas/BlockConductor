package com.ferreusveritas.node.provider;

import com.ferreusveritas.api.Request;
import com.ferreusveritas.block.Block;
import com.ferreusveritas.block.BlockCache;
import com.ferreusveritas.block.Blocks;
import com.ferreusveritas.block.misc.TerrainMap;
import com.ferreusveritas.block.misc.TerrainScanner;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.node.NodeLoader;
import com.ferreusveritas.node.shape.Shape;
import com.ferreusveritas.scene.LoaderSystem;
import com.ferreusveritas.support.json.JsonObj;

import java.util.Optional;
import java.util.UUID;

public class TerrainBlockProvider extends BlockProvider {
	
	public static final String TYPE = "terrain";
	public static final String FILL = "fill";
	public static final String SURFACE = "surface";
	public static final String SUBSURFACE = "subsurface";
	
	private final Shape shape;
	private final Block fill;
	private final Block surface;
	private final Block subSurface;
	private final AABBI aabb;
	
	private TerrainBlockProvider(UUID uuid, Shape shape, Block fill, Block surface, Block subSurface) {
		super(uuid);
		this.shape = shape;
		this.fill = fill;
		this.surface = surface;
		this.subSurface = subSurface;
		this.aabb = calculateBounds(shape);
	}
	
	private AABBI calculateBounds(Shape shape) {
		return shape.bounds().toAABBI();
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public Optional<Blocks> getBlocks(Request request) {
		AABBI area = request.area();
		AABBI bounds = intersect(area);
		if(bounds == AABBI.EMPTY) {
			return Optional.empty();
		}
		TerrainScanner scanner = new TerrainScanner();
		TerrainMap terrainMap = scanner.scanSurface(shape, bounds);
		Blocks blocks = new Blocks(bounds.size());
		
		for (int x = 0; x < terrainMap.getXSize(); x++) {
			for (int z = 0; z < terrainMap.getZSize(); z++) {
				processColumn(x, z, bounds, terrainMap, blocks);
			}
		}
		
		return Optional.of(blocks);
	}
	
	private void processColumn(int x, int z, AABBI area, TerrainMap terrainMap, Blocks blocks) {
		
		int yBottom = area.min().y();
		int terrY = terrainMap.get(x, z);
		int yTop = terrY - yBottom;
		
		if(terrY == Integer.MIN_VALUE) {
			return;
		}
		
		blocks.set(new Vec3I(x, yTop, z), surface);
			
		for (int y = yTop; y >= 0; y--) {
			Vec3I rel = new Vec3I(x, y, z);
			Vec3I abs = rel.add(area.min());

			if(shape.isInside(abs)) {
				Block block = fill;
				if (y == yTop) {
					block = surface;
				}
				if (y < yTop && y >= yTop - 3) {
					block = subSurface;
				}
				blocks.set(rel, block);
			}
		}
	}
	
	@Override
	public AABBI getAABB() {
		return aabb;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(SHAPE, shape)
			.set(FILL, fill)
			.set(SURFACE, surface)
			.set(SUBSURFACE, subSurface);
	}
	
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder {
		
		private UUID uuid = null;
		private Shape shape = null;
		private Block fill = BlockCache.NONE;
		private Block surface = BlockCache.NONE;
		private Block subSurface = BlockCache.NONE;
		
		public Builder uuid(UUID uuid) {
			this.uuid = uuid;
			return this;
		}
		
		public Builder shape(Shape shape) {
			this.shape = shape;
			return this;
		}
		
		public Builder fill(Block fill) {
			this.fill = fill;
			return this;
		}
		
		public Builder surface(Block surface) {
			this.surface = surface;
			return this;
		}
		
		public Builder subSurface(Block subSurface) {
			this.subSurface = subSurface;
			return this;
		}
		
		public TerrainBlockProvider build() {
			if(shape == null) {
				throw new IllegalStateException("shape cannot be null");
			}
			return new TerrainBlockProvider(uuid, shape, fill, surface, subSurface);
		}
		
	}
	
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends NodeLoader {
		
		private final NodeLoader shape;
		private final Block fill;
		private final Block surface;
		private final Block subSurface;
		
		public Loader(LoaderSystem loaderSystem, JsonObj src) {
			super(loaderSystem, src);
			this.shape = loaderSystem.loader(src, SHAPE);
			this.fill = loaderSystem.blockLoader(src, FILL);
			this.surface = loaderSystem.blockLoader(src, SURFACE);
			this.subSurface = loaderSystem.blockLoader(src, SUBSURFACE);
		}
		
		@Override
		public BlockProvider load(LoaderSystem loaderSystem) {
			Shape s = shape.load(loaderSystem, Shape.class).orElseThrow(wrongType(SHAPE));
			return new TerrainBlockProvider(getUuid(), s, fill, surface, subSurface);
		}
		
	}
	
}
