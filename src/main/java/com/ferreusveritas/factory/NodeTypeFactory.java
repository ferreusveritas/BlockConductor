package com.ferreusveritas.factory;

import com.ferreusveritas.node.NodeLoader;
import com.ferreusveritas.scene.LoaderSystem;
import com.ferreusveritas.support.json.InvalidJsonProperty;
import com.ferreusveritas.support.json.JsonObj;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NodeTypeFactory {
	
	private final Map<String, NodeTypeFactoryPress> factoryMap;
	
	private NodeTypeFactory(Builder builder) {
		this.factoryMap = Map.copyOf(builder.factoryMap);
	}
	
	public NodeLoader create(LoaderSystem loaderSystem, UUID uuid, JsonObj src) {
		String type = src.getString("type").orElseThrow(
			() -> new InvalidJsonProperty("Missing type")
		);
		NodeTypeFactoryPress press = factoryMap.get(type);
		if(press == null) {
			throw new InvalidJsonProperty("Unknown type: " + type);
		}
		
		return press.press(loaderSystem, uuid, src);
	}
	
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder {
		
		private final Map<String, NodeTypeFactoryPress> factoryMap = new HashMap<>();
		
		public Builder add(String type, NodeTypeFactoryPress factory) {
			factoryMap.put(type, factory);
			return this;
		}
		
		public NodeTypeFactory build() {
			return new NodeTypeFactory(this);
		}
		
	}

}