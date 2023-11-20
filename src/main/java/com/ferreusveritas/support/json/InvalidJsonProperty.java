package com.ferreusveritas.support.json;

public class InvalidJsonProperty extends RuntimeException {
    
    public InvalidJsonProperty(String message) {
        super(message);
    }
    
    public InvalidJsonProperty(String message, Exception cause) {
        super(message, cause);
    }
    
}
