package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.exception;

/**
 * Exception levée lorsqu'une ressource demandée n'est pas trouvée
 */
public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

