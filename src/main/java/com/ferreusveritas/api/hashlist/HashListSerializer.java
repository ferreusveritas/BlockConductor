package com.ferreusveritas.api.hashlist;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class HashListSerializer extends StdSerializer<HashList<?>> {

	protected HashListSerializer() {
		super(HashList.class, false);
	}

	@Override
	public void serialize(HashList hashList, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeStartArray();
		for (Object value : hashList.getList()) {
			gen.writeObject(value);
		}
		gen.writeEndArray();
	}

}
