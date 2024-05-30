package com.ferreusveritas.misc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ferreusveritas.support.exceptions.ReadException;
import com.ferreusveritas.support.exceptions.WriteException;

import java.io.IOException;
import java.io.InputStream;

public class CoreMiscHelper {
	
	public static final ObjectMapper MAPPER = new ObjectMapper();
	
	private final Class<?> clazz;
	
	public CoreMiscHelper(Class<?> clazz) {
		this.clazz = clazz;
	}
	
	public <T> T read(String jsonString, Class<T> type) {
		try {
			return MAPPER.readValue(jsonString, type);
		} catch (JsonProcessingException e) {
			throw badJsonRead(e);
		}
	}
	
	public <T> T read(JsonNode jsonNode, Class<T> type) {
		try {
			return MAPPER.treeToValue(jsonNode, type);
		} catch (JsonProcessingException e) {
			throw badJsonRead(e);
		}
	}
	
	public <T> T read(String jsonStr, TypeReference<T> typeRef) {
		try {
			return MAPPER.readValue(jsonStr, typeRef);
		} catch (JsonProcessingException e) {
			throw badJsonRead(e);
		}
	}
	
	public <T> T read(String jsonStr, JavaType javaType) {
		try {
			return MAPPER.readValue(jsonStr, javaType);
		} catch (JsonProcessingException e) {
			throw badJsonRead(e);
		}
	}
	
	private ReadException badJsonRead(Exception e) {
		return new ReadException("Could not read JSON", e);
	}
	
	public String write(Object object) {
		try {
			return MAPPER.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new WriteException("Could not write JSON", e);
		}
	}
	
	public String readResourceAsString(String path) {
		try(InputStream stream = readResourceAsStream(path)) {
			return new String(stream.readAllBytes());
		} catch (IOException e) {
			throw new ReadException("Could not read resource: " + path, e);
		}
	}
	
	public InputStream readResourceAsStream(String path) {
		return readResourceAsStream(path, clazz);
	}
	
	private static InputStream readResourceAsStream(String path, Class<?> clazz) {
		InputStream stream = clazz.getClassLoader().getResourceAsStream(path);
		if (stream == null) {
			throw new ReadException("Could not find resource: " + path);
		}
		return stream;
	}
	
	public <T> T readResourceAs(String path, Class<T> type) {
		return read(readResourceAsString(path), type);
	}
	
}
