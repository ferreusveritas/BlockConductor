package com.ferreusveritas.node;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ferreusveritas.misc.MiscHelper;

import java.io.IOException;

public class NodeDeserializer extends JsonDeserializer<Node> {
	
	@Override
	public Node deserialize(JsonParser p, DeserializationContext context) throws IOException {
		JsonNode node = p.getCodec().readTree(p);
		if(node instanceof ObjectNode objectNode) {
			return loadNode(objectNode);
		}
		throw new IllegalArgumentException("Node data is not an object");
	}
	
	private Node loadNode(ObjectNode objectNode) {
		String type = extractType(objectNode);
		var nodeClass = getNodeClass(type);
		return MiscHelper.read(objectNode, nodeClass);
	}
	
	/**
	 * Extract the type from the object node and remove it from the node
	 * Warning (Side Effects). This method modifies the object node
	 * @param objectNode The object node to extract the type from
	 * @return The type of the node
	 */
	private String extractType(ObjectNode objectNode) {
		JsonNode typeNode = objectNode.get("type");
		if(typeNode == null || !typeNode.isTextual()) {
			throw new IllegalArgumentException("Node type not specified");
		}
		objectNode.remove("type");
		return typeNode.asText();
	}
	
	private Class<? extends Node> getNodeClass(String type) {
		return Registry.getRegistryData(type).loaderClass();
	}
	
}
