package com.ferreusveritas.image;

public class ImageLoaderException extends RuntimeException {
	
	public ImageLoaderException(String message, Exception cause) {
		super(message, cause);
	}
	
	public ImageLoaderException(String message) {
		super(message);
	}
	
}
