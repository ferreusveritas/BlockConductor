package com.ferreusveritas;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ferreusveritas.misc.CoreMiscHelper;

import java.io.InputStream;

public class BaseTestSupport {
	
	private static final CoreMiscHelper CORE = new CoreMiscHelper(BaseTestSupport.class);
	
	protected static ObjectMapper mapper() {
		return CoreMiscHelper.MAPPER;
	}
	
	protected static <T> T read(String jsonString, Class<T> type) {
		return CORE.read(jsonString, type);
	}
	
	protected static <T> T read(JsonNode jsonNode, Class<T> type) {
		return CORE.read(jsonNode, type);
	}
	
	protected static <T> T read(String jsonStr, TypeReference<T> typeRef) {
		return CORE.read(jsonStr, typeRef);
	}
	
	protected static <T> T read(String jsonStr, JavaType javaType) {
		return CORE.read(jsonStr, javaType);
	}
	
	protected static String write(Object object) {
		return CORE.write(object);
	}
	
	protected static String readResourceAsString(String path) {
		return CORE.readResourceAsString(path);
	}
	
	protected static InputStream readResourceAsStream(String path) {
		return CORE.readResourceAsStream(path);
	}
	
	protected static <T> T readResourceAs(String path, Class<T> type) {
		return CORE.readResourceAs(path, type);
	}
	
}
