package com.ferreusveritas.node.ports;

import com.ferreusveritas.node.Node;
import com.ferreusveritas.node.Readable;

import java.util.Optional;

/**
 * Represents an input port of a node. Can be connected to a single output port of another node.
 * @param <T> the type of data that can be read from this port
 */
public class InputPort<T> extends Port<T> {
	
	private Readable<T> connection; // The output port that this input port is connected to
	
	public InputPort(String name, Node parent, PortDataType<T> type) {
		super(name, parent, type);
	}
	
	public T read() {
		return connection != null ? connection.read() : null;
	}
	
	public Optional<T> readOpt() {
		return Optional.ofNullable(read());
	}
	
	public void connect(OutputPort<T> input) {
		if(input.getParent() == getParent()) {
			throw new IllegalArgumentException("Cannot connect to an output port of the same node");
		}
		if(input.getType() != getType()) {
			throw new IllegalArgumentException("Type mismatch");
		}
		connection = input;
	}
	
	public PortDirection getDirection() {
		return PortDirection.IN;
	}

}
