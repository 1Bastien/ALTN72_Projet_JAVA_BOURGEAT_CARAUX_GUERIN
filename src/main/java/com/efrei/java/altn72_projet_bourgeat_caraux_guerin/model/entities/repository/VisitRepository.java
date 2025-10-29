package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.repository;

import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.Visit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository pour la visite
 */
public interface VisitRepository extends JpaRepository<Visit, Long> {
    /**
     * Récupère les visites par année scolaire
     * @param schoolYearId L'ID de l'année scolaire
     * @return La liste des visites
     */
    List<Visit> findBySchoolYearId(Long schoolYearId);
}
