package com.ferreusveritas;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ferreusveritas.api.Request;
import com.ferreusveritas.block.BlockTypes;
import com.ferreusveritas.block.Blocks;
import com.ferreusveritas.block.provider.*;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.scene.Scene;
import com.ferreusveritas.support.json.JsonObj;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BlockProviderFactoryTest extends BaseTest {
	
	@Test
	void testReading() throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		
		String blockProviderTestString = readResourceAsString("blockProviderTest.json");
		JsonObj blockProviderTest = JsonObj.fromJsonString(blockProviderTestString);
		
		Scene scene = new Scene();
		
		List<BlockProvider> providers = blockProviderTest.toImmutableList(scene::createBlockProvider);
		assertEquals(6, providers.size());
		
		BlockProvider provider = providers.get(0);
		assertTrue(provider instanceof TranslateBlockProvider);
		
		Request request = new Request("", new AABBI(0, 0, 0, 1, 1, 1));
		
		Blocks blocks = provider.getBlocks(request).orElseThrow();
		assertEquals(8, blocks.size().vol());
		assertEquals(BlockTypes.STONE, blocks.get(new Vec3I(0, 0, 0)));
		
		provider = providers.get(1);
		assertTrue(provider instanceof CombineBlockProvider);
		
		blocks = provider.getBlocks(request).orElseThrow();
		assertEquals(8, blocks.size().vol());
		assertEquals(BlockTypes.STONE, blocks.get(new Vec3I(0, 0, 0)));
		assertEquals(BlockTypes.AIR, blocks.get(new Vec3I(0, 1, 0)));
		
		provider = providers.get(2);
		assertTrue(provider instanceof ShapeBlockProvider);
		request = new Request("", new AABBI(0, 0, 0, 10, 10, 10));
		blocks = provider.getBlocks(request).orElseThrow();
		assertEquals(BlockTypes.BONE, blocks.get(new Vec3I(0, 0, 0)));
		assertEquals(BlockTypes.NONE, blocks.get(new Vec3I(4, 2, 4)));
		assertEquals(BlockTypes.BONE, blocks.get(new Vec3I(4, 2, 0)));
		assertEquals(BlockTypes.BONE, blocks.get(new Vec3I(0, 4, 0)));
		assertEquals(BlockTypes.NONE, blocks.get(new Vec3I(0, 5, 0)));
		
		provider = providers.get(3);
		assertTrue(provider instanceof MapperBlockProvider);
		
		provider = providers.get(4);
		assertTrue(provider instanceof MapperBlockProvider);
		request = new Request("", new AABBI(0, 0, 0, 1, 1, 1));
		blocks = provider.getBlocks(request).orElseThrow();
		assertEquals(BlockTypes.DIRT, blocks.get(new Vec3I(0, 0, 0)));
		
		provider = providers.get(5);
		assertTrue(provider instanceof RoutingBlockProvider);
		request = new Request("air", new AABBI(0, 0, 0, 1, 1, 1));
		blocks = provider.getBlocks(request).orElseThrow();
		assertEquals(BlockTypes.AIR, blocks.get(new Vec3I(0, 0, 0)));
		request = new Request("stone", new AABBI(0, 0, 0, 1, 1, 1));
		blocks = provider.getBlocks(request).orElseThrow();
		assertEquals(BlockTypes.STONE, blocks.get(new Vec3I(0, 0, 0)));
		
	}

}
