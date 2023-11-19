package com.ferreusveritas.support.storage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class StorageSystem {
	
	private static final String PROTOCOL_DIVIDER = "://";
	
	private final Map<String, StorageAdapter> adapters;
	
	private StorageSystem(Builder builder) {
		adapters = Map.copyOf(builder.adapters);
	}
	
	private Optional<StorageAdapter> getAdapter(String protocol) {
		return Optional.of(adapters.get(protocol));
	}
	
	public InputStream getInputStream(String path) {
		String[] parts = path.split(PROTOCOL_DIVIDER, 2);
		if(parts.length == 2) {
			String protocol = parts[0];
			String subPath = parts[1];
			return getAdapter(protocol)
				.map(adapter -> adapter.getInputStream(subPath))
				.orElseThrow(() -> new StorageException("No adapter found for protocol: " + protocol));
		}
		throw new StorageException("Invalid path: " + path);
	}
	
	public byte[] getBytes(String path) {
		try {
			return getInputStream(path).readAllBytes();
		} catch (IOException e) {
			throw new StorageException("Unable to read bytes from: " + path, e);
		}
	}
	
	public String getString(String path) {
		return new String(getBytes(path), StandardCharsets.UTF_8);
	}
	
	
	////////////////////////////////////////////////////////////////
	// Builder
	////////////////////////////////////////////////////////////////
	
	public static class Builder {
		
		private final Map<String, StorageAdapter> adapters = new HashMap<>();
		
		public Builder addAdapter(StorageAdapter adapter) {
			adapters.put(adapter.protocol(), adapter);
			return this;
		}
		
		public StorageSystem build() {
			return new StorageSystem(this);
		}
		
	}
	
}
