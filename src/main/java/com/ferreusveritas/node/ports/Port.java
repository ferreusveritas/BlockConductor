package com.ferreusveritas.node.ports;

import com.ferreusveritas.node.Node;

public abstract class Port<T> {
	
	private final String name; // The name of this input port
	private final Node parent; // The node that this input port belongs to
	private final PortDataType<T> type; // The type of data that can be read from this port
	
	protected Port(String name, Node parent, PortDataType<T> type) {
		this.name = name;
		this.parent = parent;
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	
	public Node getParent() {
		return parent;
	}
	
	public PortDataType<T> getDataType() {
		return type;
	}
	
	public Class<T> getType() {
		return type.type();
	}
	
	public abstract PortDirection getDirection();
	
}
