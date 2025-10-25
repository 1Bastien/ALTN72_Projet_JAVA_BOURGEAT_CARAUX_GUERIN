package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.repository;

import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.SchoolYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository pour l'année scolaire
 */
@Repository
public interface SchoolYearRepository extends JpaRepository<SchoolYear, Long> {

    /**
     * Récupère la liste des années scolaires existantes
     * @return La liste des années scolaires distinctes
     */
    @Query("SELECT DISTINCT sy.academicYear FROM SchoolYear sy ORDER BY sy.academicYear DESC")
    List<String> findDistinctAcademicYearByOrderByAcademicYearDesc();
}