package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Gestionnaire global des exceptions de l'application
 * Convertit les exceptions métier en réponses HTTP appropriées
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Gère les exceptions ResourceNotFoundException
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFoundException(
            ResourceNotFoundException ex, 
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {
        
        logger.error("Ressource non trouvée : {}", ex.getMessage());
        
        if (isManagementRequest(request)) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:" + getRedirectPath(request);
        }
        
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
    }

    /**
     * Gère les exceptions DatabaseException (erreurs de base de données)
     */
    @ExceptionHandler(DatabaseException.class)
    public String handleDatabaseException(
            DatabaseException ex, 
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {
        
        logger.error("Erreur de base de données : {}", ex.getMessage(), ex);
        
        if (isManagementRequest(request)) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Une erreur de base de données est survenue. Veuillez réessayer.");
            return "redirect:" + getRedirectPath(request);
        }
        
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
    }

    /**
     * Gère les exceptions DataIntegrityViolationException (contraintes de base de données)
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public String handleDataIntegrityViolationException(
            DataIntegrityViolationException ex, 
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {
        
        logger.error("Violation de contrainte d'intégrité : {}", ex.getMessage());
        
        String errorMessage = determineIntegrityViolationMessage(request, ex);
        redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        
        return "redirect:" + getRedirectPath(request);
    }

    /**
     * Vérifie si la requête concerne les pages de gestion
     */
    private boolean isManagementRequest(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path != null && path.contains("/management/");
    }

    /**
     * Détermine le chemin de redirection en fonction de la requête
     */
    private String getRedirectPath(HttpServletRequest request) {
        String path = request.getRequestURI();
        
        if (path.contains("/management/mentors")) {
            return "/management/mentors";
        } else if (path.contains("/management/companies")) {
            return "/management/companies";
        } else if (path.contains("/student/")) {
            String studentId = extractStudentId(path);
            return studentId != null ? "/student/" + studentId : "/";
        } else if (path.contains("/archives")) {
            return "/archives";
        }
        
        return "/";
    }
    
    /**
     * Extrait l'ID de l'étudiant du path
     */
    private String extractStudentId(String path) {
        String[] parts = path.split("/");
        for (int i = 0; i < parts.length; i++) {
            if ("student".equals(parts[i]) && i + 1 < parts.length) {
                return parts[i + 1];
            }
        }
        return null;
    }

    /**
     * Détermine le message d'erreur approprié pour les violations d'intégrité
     */
    private String determineIntegrityViolationMessage(HttpServletRequest request, DataIntegrityViolationException ex) {
        String path = request.getRequestURI();
        String exceptionMessage = ex.getMessage() != null ? ex.getMessage().toLowerCase() : "";
        
        if (exceptionMessage.contains("student_email_key") || exceptionMessage.contains("email")) {
            return "Cette adresse email est déjà utilisée par un autre étudiant";
        }
        
        if (path.contains("/delete")) {
            if (path.contains("/companies")) {
                return "Impossible de supprimer l'entreprise car elle est associée à des mentors ou des années scolaires";
            } else if (path.contains("/mentors")) {
                return "Impossible de supprimer le maître d'apprentissage car il est associé à des années scolaires ou des étudiants";
            } else if (path.contains("/student") || path.contains("/archives")) {
                return "Impossible de supprimer l'étudiant car il est associé à des années scolaires";
            }
            return "Impossible de supprimer cette ressource car elle est référencée par d'autres entités";
        }
        
        if (path.contains("/mentors")) {
            return "L'adresse email est déjà utilisée ou une contrainte d'unicité a été violée";
        } else if (path.contains("/companies")) {
            return "Une entreprise avec ce nom existe déjà ou une contrainte d'unicité a été violée";
        }
        
        return "Une contrainte d'unicité ou d'intégrité a été violée";
    }
}

