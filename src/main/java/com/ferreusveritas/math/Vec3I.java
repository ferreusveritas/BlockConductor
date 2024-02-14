package com.ferreusveritas.math;

import com.ferreusveritas.support.json.JsonObj;
import com.ferreusveritas.support.json.Jsonable;
import com.ferreusveritas.support.nbt.Nbtable;
import net.querz.nbt.tag.CompoundTag;

public record Vec3I(
	int x,
	int y,
	int z
) implements Jsonable, Nbtable {
	
	public static final Vec3I ZERO = new Vec3I(0);
	public static final Vec3I ONE = new Vec3I(1);
	public static final Vec3I MIN = new Vec3I(Integer.MIN_VALUE);
	public static final Vec3I MAX = new Vec3I(Integer.MAX_VALUE);
	public static final Vec3I DOWN = ZERO.withY(-1);
	public static final Vec3I UP = ZERO.withY(1);
	public static final Vec3I NORTH = ZERO.withZ(-1);
	public static final Vec3I SOUTH = ZERO.withZ(1);
	public static final Vec3I WEST = ZERO.withX(-1);
	public static final Vec3I EAST = ZERO.withX(1);
	public static final Vec3I CHUNK = new Vec3I(16, 16, 16);
	
	public Vec3I(int value) {
		this(value, value, value);
	}
	
	public Vec3I(Vec3D vec) {
		this((int)Math.floor(vec.x()), (int)Math.floor(vec.y()), (int)Math.floor(vec.z()));
	}
	
	public Vec3I(JsonObj src) {
		this(
			src.getInt("x").orElse(0),
			src.getInt("y").orElse(0),
			src.getInt("z").orElse(0)
		);
	}
	
	public Vec3D toVecD() {
		return new Vec3D(x + 0.5, y + 0.5, z + 0.5);
	}
	
	public AABBI toAABBI() {
		return new AABBI(this);
	}
	
	public Vec3I withX(int x) {
		return new Vec3I(x, y, z);
	}
	
	public Vec3I withY(int y) {
		return new Vec3I(x, y, z);
	}
	
	public Vec3I withZ(int z) {
		return new Vec3I(x, y, z);
	}
	
	public Vec3I add(Vec3I pos) {
		int x = (this.x != Integer.MIN_VALUE && this.x != Integer.MAX_VALUE) ? this.x + pos.x : this.x;
		int y = (this.y != Integer.MIN_VALUE && this.y != Integer.MAX_VALUE) ? this.y + pos.y : this.y;
		int z = (this.z != Integer.MIN_VALUE && this.z != Integer.MAX_VALUE) ? this.z + pos.z : this.z;
		return new Vec3I(x, y, z);
	}

	public Vec3I sub(Vec3I pos) {
		return add(pos.neg());
	}

	public int vol() {
		return x * y * z;
	}

	public Vec3I neg() {
		return new Vec3I(-x, -y, -z);
	}
	
	public Vec3I mul(int scale) {
		return new Vec3I(x * scale, y * scale, z * scale);
	}
	
	public Vec3I mul(double scale) {
		return new Vec3D(this).mul(scale).toVecI();
	}
	
	/**
	 * Returns the maximum value of the X, Y and Z components of the two vectors.
	 * @param pos The other vector
	 * @return A new vector with the maximum X, Y and Z components of the two vectors.
	 */
	public Vec3I max(Vec3I pos) {
		return new Vec3I(Math.max(x, pos.x), Math.max(y, pos.y), Math.max(z, pos.z));
	}

	/**
	 * Returns the minimum value of the X, Y and Z components of the two vectors.
	 * @param pos The other vector
	 * @return A new vector with the minimum X, Y and Z components of the two vectors.
	 */
	public Vec3I min(Vec3I pos) {
		return new Vec3I(Math.min(x, pos.x), Math.min(y, pos.y), Math.min(z, pos.z));
	}
	
	public double distanceTo(Vec3I center) {
		return Math.sqrt(distanceSqTo(center));
	}

	public double distanceSqTo(Vec3I center) {
		double dx = (x - center.x);
		double dy = (y - center.y);
		double dz = (z - center.z);
		return dx * dx + dy * dy + dz * dz;
	}

	public int lenSq() {
		return x * x + y * y + z * z;
	}

	public double len() {
		return Math.sqrt(lenSq());
	}
	
	public int dot(Vec3I pos) {
		return x * pos.x + y * pos.y + z * pos.z;
	}
	
	public CompoundTag toNBT() {
		CompoundTag tag = new CompoundTag();
		tag.putInt("x", x);
		tag.putInt("y", y);
		tag.putInt("z", z);
		return tag;
	}
	
	public Vec3I down() {
		return add(DOWN);
	}
	
	public Vec3I down(int count) {
		return add(DOWN.mul(count));
	}
	
	public Vec3I up() {
		return add(UP);
	}
	
	public Vec3I up(int count) {
		return add(UP.mul(count));
	}
	
	public Vec3I north() {
		return add(NORTH);
	}
	
	public Vec3I north(int count) {
		return add(NORTH.mul(count));
	}
	
	public Vec3I south() {
		return add(SOUTH);
	}
	
	public Vec3I south(int count) {
		return add(SOUTH.mul(count));
	}
	
	public Vec3I west() {
		return add(WEST);
	}
	
	public Vec3I west(int count) {
		return add(WEST.mul(count));
	}
	
	public Vec3I east() {
		return add(EAST);
	}
	
	public Vec3I east(int count) {
		return add(EAST.mul(count));
	}
	
	public int calcIndex(Vec3I size) {
		//Indices for the Blocks and Data arrays are ordered YZX - that is, the X coordinate varies the fastest.
		return x + z * size.x() + y * size.x() * size.z();
	}
	
	public static Vec3I unfoldIndex(int index, Vec3I size) {
		int x = index % size.x;
		int y = index / (size.x * size.y);
		int z = (index / size.x) % size.y;
		return new Vec3I(x, y, z);
	}
	
	public Vec3I resolve() {
		if(this == MIN) {
			return MIN;
		}
		if(this == MAX) {
			return MAX;
		}
		if(this == ZERO) {
			return ZERO;
		}
		if(x == Integer.MIN_VALUE && y == Integer.MIN_VALUE && z == Integer.MIN_VALUE) {
			return MIN;
		}
		if(x == Integer.MAX_VALUE && y == Integer.MAX_VALUE && z == Integer.MAX_VALUE) {
			return MAX;
		}
		if(x == 0 && y == 0 && z == 0) {
			return ZERO;
		}
		return this;
	}
	
	@Override
	public JsonObj toJsonObj() {
		return JsonObj.newMap()
			.set("x", x)
			.set("y", y)
			.set("z", z);
	}
	
	@Override
	public String toString() {
		return toJsonObj().toString();
	}
	
}
