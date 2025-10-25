package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.repository;

import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository pour l'étudiant
 */
public interface StudentRepository extends JpaRepository<Student, Long> {

    /**
     * Récupère la liste des étudiants pour une année scolaire spécifique
     * @param academicYear L'année scolaire
     * @return La liste des étudiants
     */
    List<Student> findBySchoolYears_academicYear(String academicYear);
}
