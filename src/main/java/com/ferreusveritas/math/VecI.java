package com.ferreusveritas.math;

import net.querz.nbt.tag.CompoundTag;

public record VecI(
	int x,
	int y,
	int z
) {
	
	public static final VecI ZERO = new VecI(0, 0, 0);
	public static final VecI MIN = new VecI(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
	public static final VecI MAX = new VecI(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
	public static final VecI DOWN = ZERO.withY(-1);
	public static final VecI UP = ZERO.withY(1);
	public static final VecI NORTH = ZERO.withZ(-1);
	public static final VecI SOUTH = ZERO.withZ(1);
	public static final VecI WEST = ZERO.withX(-1);
	public static final VecI EAST = ZERO.withX(1);
	
	public VecI(VecD vec) {
		this((int)Math.floor(vec.x()), (int)Math.floor(vec.y()), (int)Math.floor(vec.z()));
	}
	
	public VecI withX(int x) {
		return new VecI(x, y, z);
	}
	
	public VecI withY(int y) {
		return new VecI(x, y, z);
	}
	
	public VecI withZ(int z) {
		return new VecI(x, y, z);
	}
	
	public VecI add(VecI pos) {
		int x = (this.x != Integer.MIN_VALUE && this.x != Integer.MAX_VALUE) ? this.x + pos.x : this.x;
		int y = (this.y != Integer.MIN_VALUE && this.y != Integer.MAX_VALUE) ? this.y + pos.y : this.y;
		int z = (this.z != Integer.MIN_VALUE && this.z != Integer.MAX_VALUE) ? this.z + pos.z : this.z;
		return new VecI(x, y, z);
	}

	public VecI sub(VecI pos) {
		return add(pos.neg());
	}

	public int vol() {
		return x * y * z;
	}

	public VecI neg() {
		return new VecI(-x, -y, -z);
	}
	
	public VecI mul(int scale) {
		return new VecI(x * scale, y * scale, z * scale);
	}
	
	public VecI mul(double scale) {
		return new VecD(this).mul(scale).toVecI();
	}
	
	/**
	 * Returns the maximum value of the X, Y and Z components of the two vectors.
	 * @param pos The other vector
	 * @return A new vector with the maximum X, Y and Z components of the two vectors.
	 */
	public VecI max(VecI pos) {
		return new VecI(Math.max(x, pos.x), Math.max(y, pos.y), Math.max(z, pos.z));
	}

	/**
	 * Returns the minimum value of the X, Y and Z components of the two vectors.
	 * @param pos The other vector
	 * @return A new vector with the minimum X, Y and Z components of the two vectors.
	 */
	public VecI min(VecI pos) {
		return new VecI(Math.min(x, pos.x), Math.min(y, pos.y), Math.min(z, pos.z));
	}
	
	public double distanceTo(VecI center) {
		return Math.sqrt(distanceSqTo(center));
	}

	public double distanceSqTo(VecI center) {
		double dx = x - center.x;
		double dy = y - center.y;
		double dz = z - center.z;
		return dx * dx + dy * dy + dz * dz;
	}

	public int lenSq() {
		return x * x + y * y + z * z;
	}

	public double len() {
		return Math.sqrt(lenSq());
	}
	
	public int dot(VecI pos) {
		return x * pos.x + y * pos.y + z * pos.z;
	}
	
	public CompoundTag toNBT() {
		CompoundTag tag = new CompoundTag();
		tag.putInt("x", x);
		tag.putInt("y", y);
		tag.putInt("z", z);
		return tag;
	}
	
	public VecI down() {
		return add(DOWN);
	}
	
	public VecI down(int count) {
		return add(DOWN.mul(count));
	}
	
	public VecI up() {
		return add(UP);
	}
	
	public VecI up(int count) {
		return add(UP.mul(count));
	}
	
	public VecI north() {
		return add(NORTH);
	}
	
	public VecI north(int count) {
		return add(NORTH.mul(count));
	}
	
	public VecI south() {
		return add(SOUTH);
	}
	
	public VecI south(int count) {
		return add(SOUTH.mul(count));
	}
	
	public VecI west() {
		return add(WEST);
	}
	
	public VecI west(int count) {
		return add(WEST.mul(count));
	}
	
	public VecI east() {
		return add(EAST);
	}
	
	public VecI east(int count) {
		return add(EAST.mul(count));
	}
	
	public int calcIndex(VecI size) {
		//Indices for the Blocks and Data arrays are ordered YZX - that is, the X coordinate varies the fastest.
		return x + z * size.x() + y * size.x() * size.z();
	}
	
	public static VecI unfoldIndex(int index, VecI size) {
		int x = index % size.x;
		int y = index / (size.x * size.y);
		int z = (index / size.x) % size.y;
		return new VecI(x, y, z);
	}
	
}
