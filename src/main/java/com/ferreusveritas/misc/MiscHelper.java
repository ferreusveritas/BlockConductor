package com.ferreusveritas.misc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;

public class MiscHelper {
	
	private static final CoreMiscHelper CORE = new CoreMiscHelper(MiscHelper.class);
	
	public static ObjectMapper mapper() {
		return CoreMiscHelper.MAPPER;
	}
	
	public static <T> T read(String jsonString, Class<T> type) {
		return CORE.read(jsonString, type);
	}
	
	public static <T> T read(JsonNode jsonNode, Class<T> type) {
		return CORE.read(jsonNode, type);
	}
	
	public static <T> T read(String jsonStr, TypeReference<T> typeRef) {
		return CORE.read(jsonStr, typeRef);
	}
	
	public static <T> T read(String jsonStr, JavaType javaType) {
		return CORE.read(jsonStr, javaType);
	}
	
	public static String write(Object object) {
		return CORE.write(object);
	}
	
	public static String readResourceAsString(String path) {
		return CORE.readResourceAsString(path);
	}
	
	public static InputStream readResourceAsStream(String path) {
		return CORE.readResourceAsStream(path);
	}
	
	public static <T> T readResourceAs(String path, Class<T> type) {
		return CORE.readResourceAs(path, type);
	}
	
	public static void require(Object obj, String objName) {
		require(obj != null, objName + " cannot be null");
	}
	
	public static void require(boolean condition, String message) {
		if(!condition) {
			throw new IllegalArgumentException(message);
		}
	}
	
	private MiscHelper() {
		throw new UnsupportedOperationException("This class cannot be instantiated");
	}
	
}
