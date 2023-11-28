package com.ferreusveritas;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ferreusveritas.api.hashlist.HashList;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HashListTest {

	@Test
	void testHashList() throws JsonProcessingException {

		String val1 = "Hello";
		String val2 = "World";
		String val3 = "Stamp";

		HashList<String> hashList = new HashList<>();

		int i1 = hashList.add(val1);
		int i2 = hashList.add(val2);

		assertEquals(2, hashList.size());

		// Ensure values are accessible
		assertEquals(0, i1);
		assertEquals(1, i2);
		assertEquals(val1, hashList.get(0).orElse(null));
		assertEquals(val2, hashList.get(1).orElse(null));

		// Ensure re-added values return the same index
		assertEquals(0, hashList.add(val1));
		assertEquals(1, hashList.add(val2));
		
	}

}
