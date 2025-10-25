package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.service;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto.StudentDTO;

/**
 * Service pour la gestion du dashboard
 */
public interface DashboardService {
    /**
     * Prépare le modèle pour le dashboard
     * @param model Le modèle
     * @param academicYear L'année scolaire
     * @param showModal Si le modal doit être affiché
     */
    void prepareDashboardModel(Model model, String academicYear, boolean showModal);

    /**
     * Prépare le modèle pour une possibilité d'erreur lors de la création d'un étudiant
     * @param model Le modèle
     * @param studentDTO Le DTO de l'étudiant
     * @param result Les résultats de la validation
     * @param errorMessage Le message d'erreur
     */
    void prepareErrorModel(Model model, StudentDTO studentDTO, BindingResult result, String errorMessage);
}