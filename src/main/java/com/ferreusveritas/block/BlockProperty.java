package com.ferreusveritas.block;

import com.fasterxml.jackson.annotation.JsonCreator;
import net.querz.nbt.tag.*;

import java.util.Set;

public class BlockProperty {
	
	private static final Set<Class<?>> validTypes = Set.of(Boolean.class, Integer.class, String.class);
	public static final String INVALID_TYPE = "Invalid type: ";
	
	private final Object value;
	
	@JsonCreator
	public BlockProperty(Object value) {
		this.value = value;
		validate();
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
	
	public void addToNBT(CompoundTag target, String name) {
		Class<?> clazz = value.getClass();
		if(clazz.equals(Boolean.class)) {
			target.putByte(name, (byte) (Boolean.TRUE.equals(value) ? 1 : 0));
			return;
		}
		if(clazz.equals(Integer.class)) {
			target.putInt(name, (Integer) value);
			return;
		}
		if(clazz.equals(String.class)) {
			target.putString(name, (String) value);
			return;
		}
		throw new IllegalStateException(INVALID_TYPE + clazz);
	}
	
}
