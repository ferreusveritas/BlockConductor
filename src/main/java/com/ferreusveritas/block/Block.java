package com.ferreusveritas.block;

import com.ferreusveritas.support.json.JsonObj;
import com.ferreusveritas.support.json.Jsonable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A block is a 3D object that can be placed in the world.
 * A block is defined by a name and it's properties.
 * @param name A string that uniquely identifies the block type.
 * @param properties A map of properties that define the state of the block.
 */
public record Block(
	String name,
	Map<String, BlockProperty> properties
) implements Jsonable {
	
	public Block(String name, Map<String, BlockProperty> properties) {
		this.name = Objects.requireNonNull(name);
		this.properties = Map.copyOf(properties);
	}
	
	public Block(String name) {
		this(name, Map.of());
	}
	
	public Block(JsonObj src) {
		this(
			src.getString("name").orElseThrow(),
			src.getObj("properties").orElseGet(JsonObj::newMap).toImmutableMap(BlockProperty::load)
		);
	}
	
	@Override
	public JsonObj toJsonObj() {
		return JsonObj.newMap()
			.set("name", name)
			.set("properties", JsonObj.newMap(properties), !properties.isEmpty());
	}
	
	@Override
	public String toString() {
		return toJsonObj().toString();
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(name, properties);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Block block) {
			return name.equals(block.name) && properties.equals(block.properties);
		}
		return false;
	}
	
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder {
		private String name;
		private final Map<String, BlockProperty> properties = new HashMap<>();
		
		public Builder() { }
		
		public Builder(Block block) {
			name = block.name;
			properties.putAll(block.properties);
		}
		
		public Builder name(String name) {
			this.name = name;
			return this;
		}
		
		public Builder property(String name, BlockProperty property) {
			properties.put(name, property);
			return this;
		}
		
		public Builder property(String name, String value) {
			return property(name, new BlockProperty(value));
		}
		
		public Builder property(String name, int value) {
			return property(name, new BlockProperty(value));
		}
		
		public Builder property(String name, boolean value) {
			return property(name, new BlockProperty(value));
		}
		
		public Builder properties(Map<String, BlockProperty> properties) {
			this.properties.putAll(properties);
			return this;
		}
		
		public Block build() {
			return new Block(name, properties);
		}
		
	}
	
}
