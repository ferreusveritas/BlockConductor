package com.ferreusveritas.node.ports;

public record PortConnection<T>(
	InputPort<T> input,
	PortAddress from
) {
}
