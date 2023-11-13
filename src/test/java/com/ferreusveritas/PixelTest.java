package com.ferreusveritas;

import com.ferreusveritas.math.Pixel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PixelTest {

	@Test
	void test() {
		Pixel pixel = new Pixel(0, 0, 0);
		assertEquals(Pixel.BLACK, pixel);
	}

}
