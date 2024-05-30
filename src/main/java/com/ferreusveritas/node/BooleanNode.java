package com.ferreusveritas.node;

import com.ferreusveritas.node.ports.InputPort;
import com.ferreusveritas.node.ports.PortDataTypes;

import java.util.UUID;
import java.util.function.BinaryOperator;

public class BooleanNode extends Node {
	
	private final InputPort<Boolean> input1;
	private final InputPort<Boolean> input2;
	private final BinaryOperator<Boolean> operation;
	
	public BooleanNode(BinaryOperator<Boolean> operation) {
		super(UUID.randomUUID());
		createOutputPort("out", PortDataTypes.BOOLEAN, this::solve);
		this.input1 = createInputPort("in1", PortDataTypes.BOOLEAN);
		this.input2 = createInputPort("in2", PortDataTypes.BOOLEAN);
		this.operation = operation;
	}
	
	boolean solve() {
		boolean value1 = input1.readOpt().orElse(false);
		boolean value2 = input2.readOpt().orElse(false);
		return operation.apply(value1, value2);
	}
	
}
