package com.ferreusveritas.support.exceptions;

public class WriteException extends RuntimeException {

	public WriteException(String message) {
		super(message);
	}
	
	public WriteException(String message, Exception cause) {
		super(message, cause);
	}
	
}
