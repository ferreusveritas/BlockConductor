package com.ferreusveritas.node.ports;

import com.fasterxml.jackson.annotation.JsonValue;

public record PortDataType<T>(
	@JsonValue String name,
	Class<T> type
) {
}
