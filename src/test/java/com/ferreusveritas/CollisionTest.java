package com.ferreusveritas;

import com.ferreusveritas.math.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CollisionTest {

	@Test
	void test2D() {
		Line2D lineA = new Line2D(new Vec2D(0, 0), new Vec2D(10.0, 5));
		Line2D lineB = new Line2D(new Vec2D(2.0, 6.0), new Vec2D(12.0, 2.0));
		Line2D lineC = new Line2D(new Vec2D(2.0, 6.0), new Vec2D(12.0, 10.0));
		
		assertTrue(Collision2D.lineIntersectsLine(lineA, lineB));
		assertFalse(Collision2D.lineIntersectsLine(lineA, lineC));
		assertTrue(Collision2D.lineIntersectsLine(lineB, lineC));
		
		RectD rect1 = new RectD(0.0, 0.0, 15.0, 10.0);
		Vec2D[] triangle1 = new Vec2D[] { new Vec2D(0, 0), new Vec2D(10.0, 0), new Vec2D(0, 10.0) }; // Triangle overlapping upper left corner
		Vec2D[] triangle2 = new Vec2D[] { new Vec2D(7.5, -4.0), new Vec2D(13.0, 12.0), new Vec2D(2.0, 12.0) }; // Tall triangle overlapping wide rect.  No points inside each other
		Vec2D[] triangle3 = new Vec2D[] { new Vec2D(7.5, 2.0), new Vec2D(13.0, 8.0), new Vec2D(2.0, 8.0) }; // Triangle completely inside rect
		Vec2D[] triangle4 = new Vec2D[] { new Vec2D(-3.0, 7.5), new Vec2D(3.0, 14.0), new Vec2D(-5.5, 14.0) }; // Triangle outside lower left corner
		Vec2D[] triangle5 = new Vec2D[] { new Vec2D(7.5, -10.0), new Vec2D(28.0, 13.0), new Vec2D(-12.0, 13.0) }; // Square completely inside triangle
		Vec2D[] triangle6 = new Vec2D[] { new Vec2D(10.0, -2.0), new Vec2D(20.0, -2.0), new Vec2D(20.0, 7.5) }; // Triangle overlapping upper right corner
		Vec2D[] triangle7 = new Vec2D[] { new Vec2D(-2.0, 2.0), new Vec2D(8.0, 2.0), new Vec2D(-2.0, 10.0) }; // Single triangle corner inside rect
		
		assertTrue(Collision2D.rectIntersectsTriangle(rect1, triangle1));
		assertTrue(Collision2D.rectIntersectsTriangle(rect1, triangle2));
		assertTrue(Collision2D.rectIntersectsTriangle(rect1, triangle3));
		assertFalse(Collision2D.rectIntersectsTriangle(rect1, triangle4));
		assertTrue(Collision2D.rectIntersectsTriangle(rect1, triangle5));
		assertTrue(Collision2D.rectIntersectsTriangle(rect1, triangle6));
		assertTrue(Collision2D.rectIntersectsTriangle(rect1, triangle7));
		
	}
	
	@Test
	void test3D() {
		Line3D line1 = new Line3D(new Vec3D(0, 0, 0), new Vec3D(0, 20, 0));
		Line3D line2 = line1.offset(new Vec3D(-1.5, 0, -1.5));
		Line3D line3 = line1.offset(new Vec3D(1.5, 0, -1.5));
		Line3D line4 = line1.offset(new Vec3D(0.5, 0, 0.5));
		
		Vec3D[] triangle1 = new Vec3D[] { new Vec3D(-3, 10, 1), new Vec3D(1, 10, -3), new Vec3D(1, 10, 1) };
		
		assertTrue(Collision3D.lineInTriangle(line1, triangle1));
		assertFalse(Collision3D.lineInTriangle(line2, triangle1));
		assertFalse(Collision3D.lineInTriangle(line3, triangle1));
		assertTrue(Collision3D.lineInTriangle(line4, triangle1));
		
		assertFalse(Collision3D.lineInTriangle(line1.flip(), triangle1));
		assertFalse(Collision3D.lineInTriangle(line2.flip(), triangle1));
		assertFalse(Collision3D.lineInTriangle(line3.flip(), triangle1));
		assertFalse(Collision3D.lineInTriangle(line4.flip(), triangle1));
		
		
	}
	
}

