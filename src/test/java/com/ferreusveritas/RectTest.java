package com.ferreusveritas;

import com.ferreusveritas.math.RectD;
import com.ferreusveritas.math.Vec2D;
import org.junit.jupiter.api.Test;

import static junit.framework.Assert.assertTrue;

class RectTest {
	
	@Test
	void test() {
		RectD rect1 = new RectD(-43.1779, 10.2503, 0.0746, 97.759);
		assertTrue(rect1.contains(new Vec2D(-43.1779, 10.2503)));
	}
	
}
