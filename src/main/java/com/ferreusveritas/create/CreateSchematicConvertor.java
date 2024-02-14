package com.ferreusveritas.create;

import com.ferreusveritas.api.Response;
import com.ferreusveritas.api.hashlist.HashList;
import com.ferreusveritas.block.Block;
import com.ferreusveritas.block.BlockCache;
import com.ferreusveritas.block.Blocks;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;
import net.querz.nbt.tag.CompoundTag;

public class CreateSchematicConvertor {
	
	public CompoundTag convert(Response response) {
		Blocks blocks = response.blocks();
		Vec3I size = blocks.getSize();
		
		CreateSchematic.Builder builder = new CreateSchematic.Builder();
		HashList<Block> blockMap = new HashList<>();
		AABBI aabb = new AABBI(Vec3I.ZERO, size.sub(Vec3I.ONE));
		aabb.forEach((abs, rel) -> {
			Block block = blocks.get(abs);
			if(shouldInclude(block)) {
				int blockNum = blockMap.add(block);
				CreateSchematicBlock csBlock = new CreateSchematicBlock(abs, blockNum);
				builder.addBlock(csBlock);
			}
		});
		
		for (Block block : blockMap.getList()) {
			builder.addPaletteEntry(new CreateSchematicPaletteEntry(block));
		}
		
		builder.size(size);
		
		CreateSchematic schematic = builder.build();
		
		return schematic.toNBT();
	}
	
	boolean shouldInclude(Block block) {
		return block != null && !block.equals(BlockCache.NONE) && !block.equals(BlockCache.AIR);
	}


}
