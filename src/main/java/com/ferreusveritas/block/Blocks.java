package com.ferreusveritas.block;

import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.api.hashlist.HashList;
import com.ferreusveritas.support.nbt.Nbtable;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;
import net.querz.nbt.tag.ShortTag;
import net.querz.nbt.tag.StringTag;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * A 3D array of blocks with a fixed AABB.
 * The AABB is used to determine the size of the array and the position of the blocks within it.
 * @param aabb The AABB of the blocks.
 * @param size The size of the block area.
 * @param blockMap A mapping of all the blocks used in the array to block ids.
 * @param blockData An array of block ids.
 */
public record Blocks(
	AABBI aabb,
	Vec3I size,
	HashList<Block> blockMap,
	short[] blockData
) implements Nbtable {

	public Blocks(AABBI aabb) {
		this(aabb, aabb.size(), new HashList<>(), new short[aabb.size().vol()]);
		setDefaultBlocks();
	}

	public Blocks(AABBI aabb, Block block) {
		this(aabb);
		fill(block);
	}

	private void setDefaultBlocks() {
		this.blockMap.add(BlockCache.NONE);
		this.blockMap.add(BlockCache.AIR);
	}

	private int calcIndex(Vec3I pos) {
		return pos.calcIndex(size);
	}

	private boolean isValid(Vec3I pos) {
		return pos.x() >= 0 && pos.x() < size.x() && pos.y() >= 0 && pos.y() < size.y() && pos.z() >= 0 && pos.z() < size.z();
	}

	public void set(Vec3I pos, Block block) {
		if(isValid(pos)) {
			blockData[calcIndex(pos)] = (short) blockMap.add(block);
		}
	}

	public Block get(Vec3I pos) {
		if(isValid(pos)) {
			return blockMap.get(blockData[calcIndex(pos)]).orElse(BlockCache.NONE);
		}
		return BlockCache.NONE;
	}
	
	public int getIndex(Vec3I pos) {
		if(isValid(pos)) {
			return blockData[calcIndex(pos)];
		}
		return 0;
	}
	
	public void fill(Block block) {
		Arrays.fill(blockData, (short) blockMap.add(block));
	}
	
	public void print(StringBuilder builder) {
		for(int y = 0; y < size.y(); y++) {
			builder.append("Layer ").append(y).append("\n");
			for(int z = 0; z < size.z(); z++) {
				for(int x = 0; x < size.x(); x++) {
					builder.append(getIndex(new Vec3I(x, y, z))).append(" ");
				}
				builder.append("\n");
			}
			builder.append("\n");
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		if(obj == this) {
			return true;
		}
		if(obj instanceof Blocks blocks) {
			return size.equals(blocks.size) && blockMap.equals(blocks.blockMap) && Arrays.equals(blockData, blocks.blockData);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(size, blockMap, Arrays.hashCode(blockData));
	}

	@Override
	public String toString() {
		return "Blocks{" +
			"size=" + size +
			", blockMap=" + blockMap +
			", blockData=" + Arrays.toString(blockData) +
			'}';
	}
	
	@Override
	public CompoundTag toNBT() {
		CompoundTag aabbTag = aabb.toNBT();
		CompoundTag sizeTag = size.toNBT();
		
		ListTag<StringTag> blockMapTag = new ListTag<>(StringTag.class);
		List<Block> blockList = blockMap.getList();
		for(Block block : blockList) {
			blockMapTag.addString(block.toString());
		}
		
		ListTag<ShortTag> blockDataTag = new ListTag<>(ShortTag.class);
		for(short blockId : blockData) {
			blockDataTag.addShort(blockId);
		}
		
		CompoundTag main = new CompoundTag();
		main.put("aabb", aabbTag);
		main.put("size", sizeTag);
		main.put("blockMap", blockMapTag);
		main.put("blockData", blockDataTag);
		return main;
	}
	
}
