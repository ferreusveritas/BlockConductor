package com.ferreusveritas.resources;

public class ResourceLoaderException extends RuntimeException {
	
	public ResourceLoaderException(String message, Exception cause) {
		super(message, cause);
	}
	
	public ResourceLoaderException(String message) {
		super(message);
	}
	
}
