package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.service;

import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto.VisitDTO;

import java.util.List;

/**
 * Service pour la gestion des visites
 */
public interface VisitService {
    /**
     * Récupère les visites par année scolaire
     * @param schoolYearId L'ID de l'année scolaire
     * @return La liste des visites
     */
    List<VisitDTO> getVisitsBySchoolYearId(Long schoolYearId);

    /**
     * Récupère une visite par son ID
     * @param id L'ID de la visite
     * @return La visite
     */
    VisitDTO getVisitById(Long id);

    /**
     * Crée une nouvelle visite
     * @param visitDTO Le DTO de la visite
     * @return La visite créée
     */
    VisitDTO createVisit(VisitDTO visitDTO);

    /**
     * Met à jour une visite
     * @param id L'ID de la visite
     * @param visitDTO Le DTO de la visite
     * @return La visite mise à jour
     */
    VisitDTO updateVisit(Long id, VisitDTO visitDTO);

    /**
     * Supprime une visite
     * @param id L'ID de la visite
     */
    void deleteVisit(Long id);
}

