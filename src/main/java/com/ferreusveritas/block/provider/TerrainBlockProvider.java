package com.ferreusveritas.block.provider;

import com.ferreusveritas.api.Request;
import com.ferreusveritas.block.Block;
import com.ferreusveritas.block.Blocks;
import com.ferreusveritas.block.misc.TerrainMap;
import com.ferreusveritas.block.misc.TerrainScanner;
import com.ferreusveritas.shape.Shape;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;

import java.util.Optional;

public class TerrainBlockProvider extends BlockProvider {
	
	public static final String TYPE = "terrain";
	public static final String FILL = "fill";
	public static final String SURFACE = "surface";
	public static final String SUBSURFACE = "subsurface";
	
	private final Shape shape;
	private final Block fill;
	private final Block surface;
	private final Block subSurface;
	
	public TerrainBlockProvider(Scene scene, Shape shape, Block fill, Block surface, Block subSurface) {
		super(scene);
		this.shape = shape;
		this.fill = fill;
		this.surface = surface;
		this.subSurface = subSurface;
	}
	
	public TerrainBlockProvider(Scene scene, JsonObj src) {
		super(scene);
		this.shape = src.getObj(SHAPE).map(scene::createShape).orElseThrow(missing(SHAPE));
		this.fill = src.getObj(FILL).map(scene::block).orElseThrow(missing(FILL));
		this.surface = src.getObj(SURFACE).map(scene::block).orElseThrow(missing(SURFACE));
		this.subSurface = src.getObj(SUBSURFACE).map(scene::block).orElseThrow(missing(SUBSURFACE));
	}
	
	@Override
	public String getType() {
		return TYPE;
	}
	
	@Override
	public Optional<Blocks> getBlocks(Request request) {
		AABBI area = request.area();
		TerrainScanner scanner = new TerrainScanner();
		TerrainMap terrainMap = scanner.scanSurface(shape, area);
		Blocks blocks = new Blocks(area.size());
		
		for (int x = 0; x < terrainMap.getXSize(); x++) {
			for (int z = 0; z < terrainMap.getZSize(); z++) {
				processColumn(x, z, area, terrainMap, blocks);
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
		return shape.bounds().toAABBI();
	}
	
	@Override
	public JsonObj toJsonObj() {
		return super.toJsonObj()
			.set(SHAPE, shape)
			.set(FILL, fill)
			.set(SURFACE, surface)
			.set(SUBSURFACE, subSurface);
	}
	
}
