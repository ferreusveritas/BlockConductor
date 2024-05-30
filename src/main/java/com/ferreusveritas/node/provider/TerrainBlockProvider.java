package com.ferreusveritas.node.provider;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferreusveritas.api.Request;
import com.ferreusveritas.block.Block;
import com.ferreusveritas.block.Blocks;
import com.ferreusveritas.block.misc.TerrainMap;
import com.ferreusveritas.block.misc.TerrainScanner;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.node.ports.*;
import com.ferreusveritas.node.NodeRegistryData;
import com.ferreusveritas.node.shape.Shape;
import com.ferreusveritas.node.values.BlockNodeValue;

import java.util.Optional;
import java.util.UUID;

import static com.ferreusveritas.node.shape.Shape.SHAPE;

public class TerrainBlockProvider extends BlockProvider {
	
	public static final String FILL = "fill";
	public static final String SURFACE = "surface";
	public static final String SUB_SURFACE = "subSurface";
	public static final NodeRegistryData REGISTRY_DATA = new NodeRegistryData.Builder()
		.majorType(BLOCK_PROVIDER)
		.minorType("terrain")
		.loaderClass(Loader.class)
		.sceneObjectClass(TerrainBlockProvider.class)
		.value(new BlockNodeValue.Builder(FILL).build())
		.value(new BlockNodeValue.Builder(SURFACE).build())
		.value(new BlockNodeValue.Builder(SUB_SURFACE).build())
		.port(new PortDescription(PortDirection.IN, PortDataTypes.SHAPE))
		.port(new PortDescription(PortDirection.OUT, PortDataTypes.BLOCKS))
		.build();
	
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
	
	@Override
	public NodeRegistryData getRegistryData() {
		return REGISTRY_DATA;
	}
	
	private AABBI calculateBounds(Shape shape) {
		return shape.bounds().toAABBI();
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
	
	////////////////////////////////////////////////////////////////
	// Loader
	////////////////////////////////////////////////////////////////
	
	public static class Loader extends BlockProviderLoaderNode {
		
		private final InputPort<Shape> shapeInput;
		private final Block fill;
		private final Block surface;
		private final Block subSurface;
		
		@JsonCreator
		public Loader(
			@JsonProperty(UID) UUID uuid,
			@JsonProperty(SHAPE) PortAddress shapeAddress,
			@JsonProperty(FILL) Block fill,
			@JsonProperty(SURFACE) Block surface,
			@JsonProperty(SUB_SURFACE) Block subSurface
		) {
			super(uuid);
			this.shapeInput = createInputAndRegisterConnection(SHAPE, PortDataTypes.SHAPE, shapeAddress);
			this.fill = fill;
			this.surface = surface;
			this.subSurface = subSurface;
		}
		
		protected BlockProvider create() {
			return new TerrainBlockProvider(
				getUuid(),
				get(shapeInput),
				fill,
				surface,
				subSurface
			);
		}
		
	}
	
}
