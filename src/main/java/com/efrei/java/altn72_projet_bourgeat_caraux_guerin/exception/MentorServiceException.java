package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.exception;

/**
 * Exception personnalisée pour les erreurs du service Mentor
 */
public class MentorServiceException extends RuntimeException {
    
    public MentorServiceException(String message) {
        super(message);
    }
    
    public MentorServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}

