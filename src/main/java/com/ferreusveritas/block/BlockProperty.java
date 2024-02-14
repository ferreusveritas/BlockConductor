package com.ferreusveritas.block;

import com.ferreusveritas.support.json.JsonObj;
import com.ferreusveritas.support.json.Jsonable;
import net.querz.nbt.tag.*;

import java.util.Set;

public class BlockProperty implements Jsonable {
	
	private static final Set<Class<?>> validTypes = Set.of(Boolean.class, Integer.class, String.class);
	public static final String INVALID_TYPE = "Invalid type: ";
	
	private final Object value;
	
	public BlockProperty(Object value) {
		this.value = value;
		validate();
	}
	
	public static BlockProperty load(JsonObj src) {
		if(src.isString()) {
			String v = src.asString().orElseThrow();
			return new BlockProperty(v);
		}
		if(src.isNumber()) {
			Integer v = src.asInteger().orElseThrow();
			return new BlockProperty(v);
		}
		if(src.isBoolean()) {
			Boolean v = src.asBoolean().orElseThrow();
			return new BlockProperty(v);
		}
		throw new IllegalArgumentException(INVALID_TYPE + src);
	}
	
	private void validate() {
		if(value == null) {
			throw new IllegalArgumentException("Value cannot be null");
		}
		if(!validTypes.contains(value.getClass())) {
			throw new IllegalArgumentException(INVALID_TYPE + value.getClass());
		}
	}
	
	public <T> T getValue(Class<T> clazz) {
		if(!validTypes.contains(clazz)) {
			throw new IllegalArgumentException(INVALID_TYPE + clazz);
		}
		if(!clazz.isInstance(value)) {
			throw new IllegalArgumentException("Value is not of type: " + clazz);
		}
		return clazz.cast(value);
	}
	
	public boolean getBoolean() {
		return getValue(Boolean.class);
	}
	
	public int getInteger() {
		return getValue(Integer.class);
	}
	
	public String getString() {
		return getValue(String.class);
	}
	
	@Override
	public JsonObj toJsonObj() {
		Class<?> clazz = value.getClass();
		if(clazz == Boolean.class) {
			return new JsonObj((Boolean) value);
		}
		if(clazz == Integer.class) {
			return new JsonObj((Integer) value);
		}
		if(clazz == String.class) {
			return new JsonObj((String) value);
		}
		throw new IllegalStateException(INVALID_TYPE + clazz);
	}
	
	@Override
	public String toString() {
		return value.toString();
	}
	
	public void addToNBT(CompoundTag target, String name) {
		Class<?> clazz = value.getClass();
		if(clazz == Boolean.class) {
			target.putByte(name, (byte) (Boolean.TRUE.equals(value) ? 1 : 0));
		}
		if(clazz == Integer.class) {
			target.putInt(name, (Integer) value);
		}
		if(clazz == String.class) {
			target.putString(name, (String) value);
		}
		throw new IllegalStateException(INVALID_TYPE + clazz);
	}
	
}
