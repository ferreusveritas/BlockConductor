package com.ferreusveritas;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ferreusveritas.api.hashlist.HashList;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HashListTest {

	private final ObjectMapper objectMapper = new ObjectMapper();

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

		String hashListString = objectMapper.writeValueAsString(hashList);

		// Ensure serialization works
		assertEquals("[\"Hello\",\"World\"]", hashListString);

		// Ensure deserialization works
		TypeReference<HashList<String>> typeRef = new TypeReference<HashList<String>>() {};
		HashList<String> hashList2 = objectMapper.readValue(hashListString, typeRef);

		// Ensure deserialized values are the same
		assertEquals(hashList, hashList2);

		// Ensure values are still accessible after deserialization
		assertEquals(val1, hashList2.get(0).orElse(null));
		assertEquals(val2, hashList2.get(1).orElse(null));
		assertEquals(hashList2.get(val1).orElse(-1), i1);
		assertEquals(hashList2.get(val2).orElse(-1), i2);

		// Ensure hash codes are the same
		assertEquals(hashList.hashCode(), hashList2.hashCode());

		// Ensure new values can be added prior to deserialization
		hashList2.add(val3);
		assertEquals(3, hashList2.size());
		assertEquals(2, hashList2.get(val3).orElse(-1));
		assertEquals(val3, hashList2.get(2).orElse(null));
	}

}
