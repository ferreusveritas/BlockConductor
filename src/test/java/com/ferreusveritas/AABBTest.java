package com.ferreusveritas;

import com.ferreusveritas.api.AABB;
import com.ferreusveritas.api.VecI;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AABBTest {

	@Test
	void testAABB() {
		AABB aabb = new AABB(1, 3, 5, 13, 16, 19);

		assertEquals(new VecI(1, 3, 5), aabb.min());
		assertEquals(new VecI(13, 16, 19), aabb.max());
		assertEquals(new VecI(13, 14, 15), aabb.size());
		assertFalse(aabb.badSize());
		assertEquals(13 * 14 * 15, aabb.vol());

		AABB badAABB = new AABB(0, 0, 0, 0, 0, 0);
		assertFalse(badAABB.badSize());
		assertEquals(1, badAABB.vol());
	}

	@Test
	void testAABBMove() {
		AABB aabb = new AABB(0, 0, 0, 10, 10, 10);
		AABB moved = aabb.offset(new VecI(1, 3, 5));

		assertEquals(new VecI(1, 3, 5), moved.min());
		assertEquals(new VecI(11, 13, 15), moved.max());
	}

	@Test
	void testAABBGrow() {
		AABB aabb = new AABB(0, 0, 0, 10, 10, 10);
		AABB grown = aabb.grow(new VecI(1, 3, 5)).orElseThrow();
		AABB grown2 = aabb.grow(1).orElseThrow();

		assertEquals(new VecI(-1, -3, -5), grown.min());
		assertEquals(new VecI(11, 13, 15), grown.max());
		assertEquals(new VecI(-1, -3, -5), grown2.min());
		assertEquals(new VecI(11, 13, 15), grown2.max());
	}

	@Test
	void testAABBShrink() {
		AABB aabb = new AABB(0, 0, 0, 100, 100, 100);
		AABB shrunk = aabb.shrink(new VecI(1, 3, 5)).orElseThrow();

		assertEquals(new VecI(1, 3, 5), shrunk.min());
		assertEquals(new VecI(99, 97, 95), shrunk.max());
	}

	@Test
	void testAABBIntersects() {
		AABB aabb = new AABB(0, 0, 0, 10, 10, 10);
		AABB aabb2 = new AABB(5, 5, 5, 15, 15, 15);
		AABB aabb3 = new AABB(11, 11, 11, 20, 20, 20);

		assertTrue(aabb.intersects(aabb2));
		assertFalse(aabb.intersects(aabb3));
	}

	@Test
	void testAABBIntersect() {
		AABB aabb = new AABB(0, 0, 0, 10, 10, 10);
		AABB aabb2 = new AABB(5, 5, 5, 15, 15, 15);
		AABB aabb3 = new AABB(11, 11, 11, 20, 20, 20);

		assertEquals(new AABB(5, 5, 5, 10, 10, 10), aabb.intersect(aabb2).orElseThrow());
		assertFalse(aabb.intersect(aabb3).isPresent());
	}

	@Test
	void testAABBIsPointInside() {
		AABB aabb = new AABB(0, 0, 0, 10, 10, 10);

		assertFalse(aabb.isPointInside(new VecI(-1, 0, 0)));
		assertTrue(aabb.isPointInside(new VecI(5, 5, 5)));
		assertTrue(aabb.isPointInside(new VecI(0, 0, 0)));
		assertTrue(aabb.isPointInside(new VecI(10, 10, 10)));
		assertFalse(aabb.isPointInside(new VecI(11, 11, 11)));
	}

	@Test
	void testAABBUnion() {
		AABB aabb = new AABB(0, 0, 0, 10, 10, 10);
		AABB aabb2 = new AABB(5, 5, 5, 15, 15, 15);

		assertEquals(new AABB(0, 0, 0, 15, 15, 15), aabb.union(aabb2));
	}

}
