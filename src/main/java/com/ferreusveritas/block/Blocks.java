package com.ferreusveritas.block;

import com.fasterxml.jackson.annotation.JsonValue;
import com.ferreusveritas.api.hashlist.HashList;
import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;
import com.ferreusveritas.support.nbt.Nbtable;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;
import net.querz.nbt.tag.ShortTag;
import net.querz.nbt.tag.StringTag;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * A 3D array of blocks
 */
public class Blocks implements Nbtable {
	
	private final Vec3I size;
	private final HashList<Block> blockMap;
	private final short[] blockData;
	private AABBI active = null;
	
	public Blocks(Vec3I size) {
		this.size = size;
		this.blockMap = new HashList<>();
		this.blockData = new short[size.vol()];
		setDefaultBlocks();
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
			if(block != BlockCache.NONE) {
				active = active == null ? pos.toAABBI() : active.union(pos);
			}
			blockData[calcIndex(pos)] = (short) blockMap.add(block);
		}
	}

	public Block get(Vec3I pos) {
		if(isValid(pos)) {
			return blockMap.get(blockData[calcIndex(pos)]).orElse(BlockCache.NONE);
		}
		return BlockCache.NONE;
	}
	
	public void fill(Block block) {
		Arrays.fill(blockData, (short) blockMap.add(block));
		active = getAABB();
	}
	
	public AABBI getAABB() {
		return new AABBI(Vec3I.ZERO, size.sub(Vec3I.ONE));
	}
	
	/**
	 * Crop this block array to the given area.
	 * User is responsible for updating the request area.
	 *
	 * @param area The area to crop to. Must be inside the block array.
	 * @return The cropped block array.
	 */
	public Optional<Blocks> crop(AABBI area) {
		AABBI aabb = getAABB();
		if(!aabb.contains(area)) {
			throw new IllegalArgumentException("Area must be completely contained within the block array");
		}
		area = aabb.intersect(area);
		Blocks cropped = new Blocks(area.size());
		area.forEach((abs, rel) -> cropped.set(rel, get(abs)));
		return Optional.of(cropped);
	}
	
	public Optional<AABBI> getActive() {
		return Optional.ofNullable(active);
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
	
	@JsonValue
	public BlocksResponse toJson() {
		return new BlocksResponse(
			size,
			blockMap.getList(),
			IntStream.range(0, blockData.length).mapToObj(s -> blockData[s]).toList()
		);
	}
	
}
