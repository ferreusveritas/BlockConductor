package com.ferreusveritas.block;

import com.ferreusveritas.api.hashlist.HashList;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.support.json.JsonObj;
import com.ferreusveritas.support.json.Jsonable;
import com.ferreusveritas.support.nbt.Nbtable;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;
import net.querz.nbt.tag.ShortTag;
import net.querz.nbt.tag.StringTag;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/**
 * A 3D array of blocks
 */
public class Blocks implements Nbtable, Jsonable {
	
	private final Vec3I size;
	private final HashList<Block> blockMap;
	private final short[] blockData;
	
	public Blocks(Vec3I size) {
		this.size = size;
		this.blockMap = new HashList<>();
		this.blockData = new short[size.vol()];
		setDefaultBlocks();
	}
	
	public Blocks(Vec3I size, Block block) {
		this(size);
		fill(block);
	}
	
	private void setDefaultBlocks() {
		this.blockMap.add(BlockCache.NONE);
		this.blockMap.add(BlockCache.AIR);
	}
	
	public Vec3I getSize() {
		return size;
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
	public CompoundTag toNBT() {
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
		main.put("size", sizeTag);
		main.put("blockMap", blockMapTag);
		main.put("blockData", blockDataTag);
		return main;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return JsonObj.newMap()
			.set("size", size)
			.set("blockMap", JsonObj.newList(blockMap.getList()))
			.set("blockData", JsonObj.newList(IntStream.range(0, blockData.length).mapToObj(s -> blockData[s]).toList()));
	}
	
	@Override
	public String toString() {
		return toJsonObj().toString();
	}
	
}
