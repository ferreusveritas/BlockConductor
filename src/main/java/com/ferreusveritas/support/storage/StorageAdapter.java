package com.ferreusveritas.support.storage;

import java.io.InputStream;

public interface StorageAdapter {
	
	String protocol();
	
	InputStream getInputStream(String path);
	
}
