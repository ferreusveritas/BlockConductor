package com.ferreusveritas.support.exceptions;

public class InvalidJsonProperty extends RuntimeException {
    
    public InvalidJsonProperty(String message) {
        super(message);
    }
    
    public InvalidJsonProperty(String message, Exception cause) {
        super(message, cause);
    }
    
}
