package com.ferreusveritas.scene;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ferreusveritas.node.*;
import com.ferreusveritas.node.ports.InputPort;
import com.ferreusveritas.node.ports.OutputPort;
import com.ferreusveritas.node.ports.PortAddress;
import com.ferreusveritas.node.ports.PortConnection;
import com.ferreusveritas.support.exceptions.PortNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public record Scene(
	Map<UUID, Node> nodes,
	Node root
) {
	
	@JsonCreator
	public static Scene create(
		@JsonProperty("nodes") List<Node> nodes,
		@JsonProperty("root") UUID root
	) {
		Map<UUID, Node> nodeMap = nodes.stream().collect(Collectors.toMap(Node::getUuid, Function.identity()));
		Node rootNode = nodeMap.get(root);
		Scene scene = new Scene(nodeMap, rootNode);
		scene.createNodeConnections();
		return scene;
	}
	
	public Optional<Node> getNode(UUID uuid) {
		return Optional.ofNullable(nodes.get(uuid));
	}
	
	public void createNodeConnections() {
		for(Node node : nodes().values()) {
			if(node instanceof LoaderNode loaderNode) {
				for (PortConnection<?> connection : loaderNode.getConnections()) {
					connect(connection);
				}
			}
		}
	}
	
	private void connect(PortConnection<?> connection) {
		InputPort<?> inputPort = connection.input();
		PortAddress from = connection.from();
		Node fromNode = getNode(from.node()).orElseThrow();
		Class<?> fromType = inputPort.getType();
		connect(fromNode, from.port(), inputPort, fromType);
	}
	
	private <T> void connect(Node fromNode, String fromPort, InputPort<?> inputPort, Class<T> type) {
		OutputPort<T> out = fromNode.getOutputPort(fromPort, type).orElseThrow(this::badPortException);
		InputPort<T> in = inputPort.getParent().getInputPort(inputPort.getName(), type).orElseThrow(this::badPortException);
		in.connect(out);
	}
	
	private RuntimeException badPortException() {
		return new PortNotFoundException("Port not found");
	}
	
}
