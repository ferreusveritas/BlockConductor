package com.ferreusveritas.math;

import net.querz.nbt.tag.CompoundTag;

import java.util.Optional;
import java.util.function.BiConsumer;

/**
 * An axis-aligned bounding box.
 */
public record AABB(
	VecI min,
	VecI max
) {
	public static final AABB NONE = new AABB(VecI.ZERO, VecI.ZERO);
	public static final AABB INFINITE = new AABB(VecI.MIN, VecI.MAX);
	
	public AABB(int x1, int y1, int z1, int x2, int y2, int z2) {
		this(new VecI(x1, y1, z1), new VecI(x2, y2, z2));
	}

	/**
	 * Returns a new AABB moved by the specified amount.
	 * @param pos The amount to move
	 * @return A new AABB moved by the specified amount.
	 */
	public AABB offset(VecI pos) {
		return new AABB(min.add(pos), max.add(pos));
	}

	/**
	 * Returns the size of this AABB.
	 * @return The size of this AABB.
	 */
	public VecI size() {
		return max.sub(min).add(new VecI(1, 1, 1));
	}

	public int vol() {
		return size().vol();
	}

	public boolean badSize() {
		VecI size = size();
		return size.x() <= 0 || size.y() <= 0 || size.z() <= 0;
	}

	private static Optional<AABB> badFilter(AABB in) {
		return in.badSize() ? Optional.empty() : Optional.of(in);
	}

	/**
	 * Returns whether a point is inside this AABB.
	 * @param pos The point
	 * @return Whether the point is inside this AABB.
	 */
	public boolean isPointInside(VecI pos) {
		return
			pos.x() >= min.x() && pos.x() <= max.x() &&
			pos.y() >= min.y() && pos.y() <= max.y() &&
			pos.z() >= min.z() && pos.z() <= max.z();
	}

	/**
	 * Returns whether this AABB intersects another AABB.
	 * @param aabb The other AABB
	 * @return Whether this AABB intersects the other AABB.
	 */
	public boolean intersects(AABB aabb) {
		return
			aabb != null &&
			min.x() <= aabb.max.x() && max.x() >= aabb.min.x() &&
			min.y() <= aabb.max.y() && max.y() >= aabb.min.y() &&
			min.z() <= aabb.max.z() && max.z() >= aabb.min.z();
	}

	/**
	 * Returns the intersection of this AABB with another AABB.
	 * @param aabb The other AABB
	 * @return The intersection of this AABB with the other AABB, or {@link Optional#empty()} if there is no intersection.
	 */
	public Optional<AABB> intersect(AABB aabb) {
		if(!intersects(aabb)) {
			return Optional.empty();
		}
		return Optional.of(new AABB(min.max(aabb.min), max.min(aabb.max)));
	}

	public Optional<AABB> grow(int amount) {
		return grow(new VecI(amount, amount, amount));
	}

	public Optional<AABB> grow(VecI amount) {
		return badFilter(new AABB(min.sub(amount), max.add(amount)));
	}

	public Optional<AABB> shrink(int amount) {
		return grow(-amount);
	}

	public Optional<AABB> shrink(VecI amount) {
		return grow(amount.neg());
	}

	public AABB union(AABB aabb) {
		return new AABB(min.min(aabb.min), max.max(aabb.max));
	}
	
	/**
	 * Iterates over all positions in this AABB.
	 * @param func A function that accepts the absolute position and the relative position respectively.
	 */
	public void forEach(BiConsumer<VecI, VecI> func) {
		VecI size = size();
		int maxX = size.x();
		int maxY = size.y();
		int maxZ = size.z();
		for(int y = 0; y < maxY; y++) {
			for(int x = 0; x < maxX; x++) {
				for(int z = 0; z < maxZ; z++) {
					VecI rel = new VecI(x, y, z);
					func.accept(rel.add(min), rel);
				}
			}
		}
	}
	
	public CompoundTag toNBT() {
		CompoundTag tag = new CompoundTag();
		tag.put("min", min().toNBT());
		tag.put("max", max().toNBT());
		return tag;
	}
	
	public AABB fromChunk(VecI chunkPos) {
		VecI blockPos = chunkPos.mul(16);
		VecI chunkSize = new VecI(16, 16, 16);
		return new AABB(blockPos, blockPos.add(chunkSize));
	}
	
}
