package com.ferreusveritas;

import com.ferreusveritas.math.AABBI;
import com.ferreusveritas.math.Vec3I;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AABBTest {

	@Test
	void testAABB() {
		AABBI aabb = new AABBI(1, 3, 5, 13, 16, 19);

		assertEquals(new Vec3I(1, 3, 5), aabb.min());
		assertEquals(new Vec3I(13, 16, 19), aabb.max());
		assertEquals(new Vec3I(13, 14, 15), aabb.size());
		assertFalse(aabb.badSize());
		assertEquals(13 * 14 * 15, aabb.vol());

		AABBI badAABB = new AABBI(0, 0, 0, 0, 0, 0);
		assertFalse(badAABB.badSize());
		assertEquals(1, badAABB.vol());
	}

	@Test
	void testAABBMove() {
		AABBI aabb = new AABBI(0, 0, 0, 10, 10, 10);
		AABBI moved = aabb.offset(new Vec3I(1, 3, 5));

		assertEquals(new Vec3I(1, 3, 5), moved.min());
		assertEquals(new Vec3I(11, 13, 15), moved.max());
	}

	@Test
	void testAABBGrow() {
		AABBI aabb = new AABBI(0, 0, 0, 10, 10, 10);
		AABBI grown = aabb.expand(new Vec3I(1, 3, 5)).orElseThrow();
		AABBI grown2 = aabb.expand(1).orElseThrow();

		assertEquals(new Vec3I(-1, -3, -5), grown.min());
		assertEquals(new Vec3I(11, 13, 15), grown.max());
		assertEquals(new Vec3I(-1, -1, -1), grown2.min());
		assertEquals(new Vec3I(11, 11, 11), grown2.max());
	}

	@Test
	void testAABBShrink() {
		AABBI aabb = new AABBI(0, 0, 0, 100, 100, 100);
		AABBI shrunk = aabb.shrink(new Vec3I(1, 3, 5)).orElseThrow();

		assertEquals(new Vec3I(1, 3, 5), shrunk.min());
		assertEquals(new Vec3I(99, 97, 95), shrunk.max());
	}

	@Test
	void testAABBIntersects() {
		AABBI aabb = new AABBI(0, 0, 0, 10, 10, 10);
		AABBI aabb2 = new AABBI(5, 5, 5, 15, 15, 15);
		AABBI aabb3 = new AABBI(11, 11, 11, 20, 20, 20);

		assertTrue(aabb.intersects(aabb2));
		assertFalse(aabb.intersects(aabb3));
	}

	@Test
	void testAABBIntersect() {
		AABBI aabb = new AABBI(0, 0, 0, 10, 10, 10);
		AABBI aabb2 = new AABBI(5, 5, 5, 15, 15, 15);
		AABBI aabb3 = new AABBI(11, 11, 11, 20, 20, 20);

		assertEquals(new AABBI(5, 5, 5, 10, 10, 10), aabb.intersect(aabb2).orElseThrow());
		assertFalse(aabb.intersect(aabb3).isPresent());
	}

	@Test
	void testAABBIsPointInside() {
		AABBI aabb = new AABBI(0, 0, 0, 10, 10, 10);

		assertFalse(aabb.isInside(new Vec3I(-1, 0, 0)));
		assertTrue(aabb.isInside(new Vec3I(5, 5, 5)));
		assertTrue(aabb.isInside(new Vec3I(0, 0, 0)));
		assertTrue(aabb.isInside(new Vec3I(10, 10, 10)));
		assertFalse(aabb.isInside(new Vec3I(11, 11, 11)));
	}

	@Test
	void testAABBUnion() {
		AABBI aabb = new AABBI(0, 0, 0, 10, 10, 10);
		AABBI aabb2 = new AABBI(5, 5, 5, 15, 15, 15);

		assertEquals(new AABBI(0, 0, 0, 15, 15, 15), aabb.union(aabb2));
	}

}
