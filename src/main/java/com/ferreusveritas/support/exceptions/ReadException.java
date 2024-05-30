package com.ferreusveritas.support.exceptions;

public class ReadException extends RuntimeException {

	public ReadException(String message) {
		super(message);
	}
	
	public ReadException(String message, Exception cause) {
		super(message, cause);
	}
	
}
