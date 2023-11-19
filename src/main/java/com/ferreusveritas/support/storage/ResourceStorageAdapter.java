package com.ferreusveritas.support.storage;

import java.io.InputStream;

public class ResourceStorageAdapter implements StorageAdapter {
	
	private static final String PROTOCOL = "res";
	
	private final ClassLoader classLoader;
	
	public ResourceStorageAdapter(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}
	
	@Override
	public String protocol() {
		return PROTOCOL;
	}
	
	@Override
	public InputStream getInputStream(String path) {
		InputStream stream = classLoader.getResourceAsStream(path);
		if(stream == null) {
			throw new StorageException("Resource unavailable: " + path);
		}
		return stream;
	}
	
}
