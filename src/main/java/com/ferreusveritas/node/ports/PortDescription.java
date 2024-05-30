package com.ferreusveritas.node.ports;

import com.ferreusveritas.node.DataSpan;

public record PortDescription(
	String name,
	PortDirection direction,
	PortDataType<?> type,
	DataSpan span
) {
	
	public PortDescription(PortDirection direction, PortDataType<?> type, DataSpan span) {
		this(type.name(), direction, type, span);
	}
	
	public PortDescription(String name, PortDirection direction, PortDataType<?> type) {
		this(name, direction, type, DataSpan.SINGLE);
	}
	
	public PortDescription(PortDirection direction, PortDataType<?> type) {
		this(type.name(), direction, type, DataSpan.SINGLE);
	}
	
}
