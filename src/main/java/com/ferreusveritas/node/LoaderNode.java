package com.ferreusveritas.node;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ferreusveritas.node.ports.*;

import java.util.*;

@JsonDeserialize
public class LoaderNode extends Node {
	
	private final List<PortConnection<?>> connections = new ArrayList<>();
	
	protected LoaderNode(UUID uuid) {
		super(uuid);
	}
	
	protected <T> void registerConnection(PortConnection<T> connection) {
		connections.add(connection);
	}
	
	protected <T> void registerConnection(InputPort<T> input, PortAddress from) {
		registerConnection(new PortConnection<>(input, from));
	}
	
	protected <T> List<InputPort<T>> createInputsAndRegisterConnections(String name, PortDataType<T> type, List<PortAddress> from) {
		List<InputPort<T>> inputPorts = new ArrayList<>(from.size());
		for(int i = 0; i < from.size(); i++) {
			inputPorts.add(createInputAndRegisterConnection(name + i, type, from.get(i)));
		}
		return List.copyOf(inputPorts);
	}
	
	protected <T> List<InputPort<T>> createInputsAndRegisterConnections(PortDataType<T> type, List<PortAddress> from) {
		if(from == null) {
			throw new NullPointerException("Input list cannot be null");
		}
		return createInputsAndRegisterConnections(type.name(), type, from);
	}
	
	protected <T> InputPort<T> createInputAndRegisterConnection(String name, PortDataType<T> type, PortAddress from) {
		InputPort<T> inputPort = createInputPort(name, type);
		registerConnection(new PortConnection<>(inputPort, from));
		return inputPort;
	}
	
	protected <T> InputPort<T> createInputAndRegisterConnection(PortDataType<T> type, PortAddress from) {
		return createInputAndRegisterConnection(type.name(), type, from);
	}
	
	public List<PortConnection<?>> getConnections() {
		return Collections.unmodifiableList(connections);
	}
	
	protected <T> T get(InputPort<T> inputPort) {
		return inputPort.readOpt().orElseThrow(missing(inputPort.getName()));
	}
	
}
