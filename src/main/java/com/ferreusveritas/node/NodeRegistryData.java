package com.ferreusveritas.node;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ferreusveritas.node.ports.PortDescription;
import com.ferreusveritas.node.ports.PortDirection;
import com.ferreusveritas.node.values.NodeValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record NodeRegistryData(
	String majorType,
	String minorType,
	@JsonIgnore Class<? extends LoaderNode> loaderClass,
	@JsonIgnore Class<? extends SceneObject> sceneObjectClass,
	List<NodeValue> values,
	@JsonIgnore Map<PortLocation, PortDescription> ports
) {
	
	public record PortLocation(String name, PortDirection direction) {}
	
	public NodeRegistryData {
		ports = Map.copyOf(ports);
	}
	
	public String type() {
		return fullTypeName(majorType, minorType);
	}
	
	private static String fullTypeName(String major, String minor) {
		return major + ":" + minor;
	}
	
	public PortDescription port(String name, PortDirection direction) {
		return ports.get(new PortLocation(name, direction));
	}
	
	public List<PortDescription> getPorts() {
		return new ArrayList<>(ports.values());
	}
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder {
		
		private String majorType;
		private String minorType;
		private Class<? extends LoaderNode> loaderClass;
		private Class<? extends SceneObject> sceneObjectClass;
		private final List<NodeValue> values = new ArrayList<>();
		private final Map<PortLocation, PortDescription> ports = new HashMap<>();
		
		public Builder majorType(String majorType) {
			this.majorType = majorType;
			return this;
		}
		
		public Builder minorType(String minorType) {
			this.minorType = minorType;
			return this;
		}
		
		public Builder loaderClass(Class<? extends LoaderNode> loaderClass) {
			this.loaderClass = loaderClass;
			return this;
		}
		
		public Builder sceneObjectClass(Class<? extends SceneObject> sceneObjectClass) {
			this.sceneObjectClass = sceneObjectClass;
			return this;
		}
		
		public Builder value(NodeValue values) {
			this.values.add(values);
			return this;
		}
		
		public Builder port(PortDescription port) {
			String portName = port.name();
			PortDirection direction = port.direction();
			PortLocation location = new PortLocation(portName, direction);
			if(ports.containsKey(location)) {
				throw new IllegalArgumentException("Port with name " + port.name() + " already exists with the same direction: " + port.direction());
			}
			this.ports.put(location, port);
			return this;
		}
		
		public NodeRegistryData build() {
			return new NodeRegistryData(
				majorType,
				minorType,
				loaderClass,
				sceneObjectClass,
				values,
				ports
			);
		}
		
	}
}
