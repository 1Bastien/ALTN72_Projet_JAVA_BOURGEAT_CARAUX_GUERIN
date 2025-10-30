package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.exception;

/**
 * Exception levée lors d'erreurs d'accès à la base de données
 */
public class DatabaseException extends RuntimeException {
    
    public DatabaseException(String message) {
        super(message);
    }
    
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}

