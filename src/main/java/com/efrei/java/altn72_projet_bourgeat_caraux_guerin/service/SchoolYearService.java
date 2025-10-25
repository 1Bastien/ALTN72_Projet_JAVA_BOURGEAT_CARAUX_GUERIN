package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.service;

import java.util.List;

/**
 * Service pour la gestion des années scolaires
 */
public interface SchoolYearService {
    /**
     * Récupère la liste des années scolaires distinctes existantes
     * @return La liste des années scolaires distinctes
     */
    List<String> getAllAcademicYears();
}
