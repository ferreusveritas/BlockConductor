package com.ferreusveritas.support.storage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FileStorageAdapter implements StorageAdapter {
	
	private static final String PROTOCOL = "file";
	
	@Override
	public String protocol() {
		return PROTOCOL;
	}
	
	@Override
	public InputStream getInputStream(String path) {
		try {
			return new FileInputStream(path);
		} catch (FileNotFoundException e) {
			throw new StorageException("File not found: " + path, e);
		}
	}
	
}
