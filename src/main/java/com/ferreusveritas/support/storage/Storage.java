package com.ferreusveritas.support.storage;

import com.ferreusveritas.support.json.JsonObj;

import java.io.InputStream;

public class Storage {
	
	private static final StorageSystem storageSystem = new StorageSystem.Builder()
		.addAdapter(new ResourceStorageAdapter(Storage.class.getClassLoader()))
		.addAdapter(new FileStorageAdapter())
		.build();
	
	public static InputStream getInputStream(String path) {
		return storageSystem.getInputStream(path);
	}
	
	public static byte[] getBytes(String path) {
		return storageSystem.getBytes(path);
	}
	
	public static String getString(String path) {
		return storageSystem.getString(path);
	}
	
	public static JsonObj getJson(String path) {
		return JsonObj.fromJsonString(storageSystem.getString(path));
	}
	
	private Storage() {
		throw new AssertionError("Utility Class");
	}
	
}
