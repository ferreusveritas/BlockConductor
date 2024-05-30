package com.ferreusveritas.node;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ferreusveritas.misc.MiscHelper;
import com.ferreusveritas.node.ports.*;
import com.ferreusveritas.support.exceptions.InvalidJsonProperty;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import static com.ferreusveritas.node.SceneObject.UID;

@JsonDeserialize(using = NodeDeserializer.class)
public abstract class Node {
	
	private final UUID uuid;
	private final HashMap<String, InputPort<?>> inputPorts = new HashMap<>();
	private final HashMap<String, OutputPort<?>> outputPorts = new HashMap<>();
	
	protected Node(UUID uuid) {
		this.uuid = uuid;
		validate();
	}
	
	public UUID getUuid() {
		return uuid;
	}
	
	private void validate() {
		MiscHelper.require(uuid, UID);
	}
	
	protected <T> InputPort<T> createInputPort(String name, PortDataType<T> type) {
		InputPort<T> port = new InputPort<>(name, this, type);
		inputPorts.put(name, port);
		return port;
	}
	
	protected <T> InputPort<T> createInputPort(PortDataType<T> type) {
		return createInputPort(type.name(), type);
	}
	
	protected <T> OutputPort<T> createOutputPort(String name, PortDataType<T> type, Supplier<T> supplier) {
		OutputPort<T> port = new OutputPort<>(name, this, type, supplier);
		outputPorts.put(name, port);
		return port;
	}
	
	protected <T> OutputPort<T> createOutputPort(PortDataType<T> type, Supplier<T> supplier) {
		return createOutputPort(type.name(), type, supplier);
	}
	
	@SuppressWarnings("unchecked")
	public <T> Optional<InputPort<T>> getInputPort(String name, Class<T> type) {
		return getInputPort(name)
			.filter(port -> port.getType() == type)
			.map(port -> (InputPort<T>) port);
	}
	
	@SuppressWarnings("unchecked")
	public <T> Optional<OutputPort<T>> getOutputPort(String name, Class<T> type) {
		return getOutputPort(name)
			.filter(port -> port.getType() == type)
			.map(port -> (OutputPort<T>) port);
	}
	
	private Optional<InputPort<?>> getInputPort(String name) {
		return Optional.ofNullable(inputPorts.get(name));
	}
	
	private Optional<OutputPort<?>> getOutputPort(String name) {
		return Optional.ofNullable(outputPorts.get(name));
	}
	
	protected static Supplier<InvalidJsonProperty> missing(String name) {
		return () -> new InvalidJsonProperty("Missing " + name);
	}
	
}
