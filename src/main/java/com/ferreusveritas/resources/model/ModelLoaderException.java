package com.ferreusveritas.resources.model;

public class ModelLoaderException extends RuntimeException {
	
	public ModelLoaderException(String message, Exception cause) {
		super(message, cause);
	}
	
	public ModelLoaderException(String message) {
		super(message);
	}
	
}
