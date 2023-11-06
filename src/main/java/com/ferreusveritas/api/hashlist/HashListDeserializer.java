package com.ferreusveritas.api.hashlist;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;

public class HashListDeserializer extends JsonDeserializer<HashList<?>> implements ContextualDeserializer {

	private JavaType valueType;

	@Override
	public JsonDeserializer<?> createContextual(DeserializationContext context, BeanProperty property) {
		JavaType wrapperType = context.getContextualType();
		HashListDeserializer deserializer = new HashListDeserializer();
		deserializer.valueType = wrapperType.containedType(0);
		return deserializer;
	}

	@Override
	public HashList<?> deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
		JsonNode node = jsonParser.getCodec().readTree(jsonParser);
		HashList<?> hashList = new HashList<>();
		if(node.isArray()) {
			ArrayNode arrayNode = (ArrayNode) node;
			for (JsonNode valueNode : arrayNode) {
				hashList.add(context.readTreeAsValue(valueNode, valueType));
			}
		}
		return hashList;
	}

}
