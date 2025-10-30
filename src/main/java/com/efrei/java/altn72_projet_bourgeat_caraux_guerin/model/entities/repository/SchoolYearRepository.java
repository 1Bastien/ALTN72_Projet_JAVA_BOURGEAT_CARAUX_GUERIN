package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.repository;

import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.SchoolYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    /**
     * Récupère les années scolaires d'un étudiant
     * @param studentId L'ID de l'étudiant
     * @return La liste des années scolaires de l'étudiant
     */
    List<SchoolYear> findByStudentId(Long studentId);

    /**
     * Récupère une année scolaire d'un étudiant pour une année académique donnée
     * @param studentId L'ID de l'étudiant
     * @param academicYear L'année académique
     * @return L'année scolaire
     */
    Optional<SchoolYear> findByStudentIdAndAcademicYear(Long studentId, String academicYear);
}