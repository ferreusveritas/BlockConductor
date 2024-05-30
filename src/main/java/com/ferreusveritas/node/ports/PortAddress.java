package com.ferreusveritas.node.ports;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.UUID;

public record PortAddress(
	UUID node,
	String port
) {
	
	@JsonCreator
	public static PortAddress create(
		String value
	) {
		String[] parts = value.split(":");
		if(parts.length != 2) {
			throw new IllegalArgumentException("Invalid PortAddress: " + value);
		}
		UUID uuid = UUID.fromString(parts[0]);
		String port = parts[1];
		return new PortAddress(uuid, port);
	}
	
	@JsonValue
	public String value() {
		return node.toString() + ":" + port;
	}
	
}
