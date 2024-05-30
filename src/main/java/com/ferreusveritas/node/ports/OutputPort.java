package com.ferreusveritas.node.ports;

import com.ferreusveritas.node.Node;
import com.ferreusveritas.node.Readable;

import java.util.function.Supplier;

public class OutputPort<T> extends Port<T> implements Readable<T> {
	
	private final Supplier<T> supplier;
	
	public OutputPort(String name, Node parent, PortDataType<T> type, Supplier<T> supplier) {
		super(name, parent, type);
		this.supplier = supplier;
	}
	
	public T read() {
		return supplier.get();
	}
	
	@Override
	public PortDirection getDirection() {
		return PortDirection.OUT;
	}
	
}
