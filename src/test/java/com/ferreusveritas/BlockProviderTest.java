package com.ferreusveritas;

import com.ferreusveritas.api.Request;
import com.ferreusveritas.block.Block;
import com.ferreusveritas.block.BlockCache;
import com.ferreusveritas.block.Blocks;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.node.ports.OutputPort;
import com.ferreusveritas.node.provider.BlockProvider;
import com.ferreusveritas.scene.Scene;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BlockProviderTest extends BaseTestSupport {
	
	private static final Block NONE = BlockCache.NONE;
	private static final Block AIR = new Block("minecraft:air");
	private static final Block DIRT = new Block("minecraft:dirt");
	private static final Block STONE = new Block("minecraft:stone");
	
	private BlockProvider readBlockProvider(String resource) {
		Scene scene = readResourceAs(resource, Scene.class);
		return scene.root().getOutputPort("blocks", BlockProvider.class).flatMap(OutputPort::readOpt).orElseThrow();
	}
	
	@Test
	void testSolidBlockProvider() {
		BlockProvider provider = readBlockProvider("solidBlockProvider.json");
		Request request = new Request("", new AABBI(0, 0, 0, 0, 0, 0));
		Blocks blocks = provider.getBlocks(request).orElseThrow();
		assertEquals(STONE, blocks.get(new Vec3I(0, 0, 0)));
		assertEquals(NONE, blocks.get(new Vec3I(1, 0, 0)));
	}
	
	@Test
	void testRouteBlockProvider() {
		BlockProvider provider = readBlockProvider("routeBlockProvider.json");
		Request request = new Request("stone", new AABBI(0, 0, 0, 0, 0, 0));
		Blocks blocks = provider.getBlocks(request).orElseThrow();
		assertEquals(STONE, blocks.get(new Vec3I(0, 0, 0)));
		request = new Request("dirt", new AABBI(1, 0, 0, 1, 0, 0));
		blocks = provider.getBlocks(request).orElseThrow();
		assertEquals(DIRT, blocks.get(new Vec3I(0, 0, 0)));
		request = new Request("air", new AABBI(0, 0, 0, 0, 0, 0));
		blocks = provider.getBlocks(request).orElseThrow();
		assertEquals(AIR, blocks.get(new Vec3I(0, 0, 0)));
	}
	
	@Test
	void testBlockMapperProvider() {
		BlockProvider provider = readBlockProvider("blockMapperProvider.json");
		Request request = new Request("", new AABBI(0, 0, 0, 0, 0, 0));
		Blocks blocks = provider.getBlocks(request).orElseThrow();
		assertEquals(DIRT, blocks.get(new Vec3I(0, 0, 0)));
	}
	
	@Test
	void testCombineBlockProvider() {
		BlockProvider provider = readBlockProvider("combineBlockProvider.json");
		Request request = new Request("", new AABBI(0, 0, 0, 0, 0, 0));
		Blocks blocks = provider.getBlocks(request).orElseThrow();
		assertEquals(STONE, blocks.get(new Vec3I(0, 0, 0)));
		request = new Request("", new AABBI(12, 0, 0, 12, 0, 0));
		blocks = provider.getBlocks(request).orElseThrow();
		assertEquals(DIRT, blocks.get(new Vec3I(0, 0, 0)));
	}
	
	@Test
	void testTranslateBlockProvider() {
		BlockProvider provider = readBlockProvider("translateBlockProvider.json");
		Request request = new Request("", new AABBI(0, 0, 0, 0, 0, 0));
		assertTrue(provider.getBlocks(request).isEmpty());
		request = new Request("", new AABBI(0, 2, 0, 0, 2, 0));
		Blocks blocks = provider.getBlocks(request).orElseThrow();
		assertEquals(STONE, blocks.get(new Vec3I(0, 0, 0)));
	}
	
}
