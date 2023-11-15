package com.ferreusveritas.support.nbt;

public class NbtException extends RuntimeException {
	
	public NbtException(String message) {
		super(message);
	}
	
	public NbtException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
