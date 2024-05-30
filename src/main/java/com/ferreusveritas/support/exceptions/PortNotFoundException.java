package com.ferreusveritas.support.exceptions;

public class PortNotFoundException extends RuntimeException {
	
	public PortNotFoundException(String message) {
		super(message);
	}
	
	public PortNotFoundException(String message, Exception cause) {
		super(message, cause);
	}
	
}
