package com.ferreusveritas.node;

import com.ferreusveritas.node.ports.OutputPort;
import com.ferreusveritas.node.ports.PortDataType;

import java.util.UUID;

public class ConstantNode<T> extends Node {

	private final T value;
	public final OutputPort<T> output;
	
	public ConstantNode(T value, PortDataType<T> type) {
		super(UUID.randomUUID());
		this.value = value;
		this.output = createOutputPort("out", type, () -> this.value);
	}
	
}
