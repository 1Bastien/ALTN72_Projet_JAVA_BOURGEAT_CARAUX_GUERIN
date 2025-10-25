package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.service;

import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto.StudentDTO;
import java.util.List;

/**
 * Service pour la gestion des étudiants
 */
public interface StudentService {
    /**
     * Récupère un étudiant par son ID
     * @param id L'ID de l'étudiant
     * @return Le DTO de l'étudiant
     */
    StudentDTO getStudentById(Long id);

    /**
     * Sauvegarde un étudiant
     * @param studentDTO Le DTO de l'étudiant
     * @return Le DTO de l'étudiant
     */
    StudentDTO saveStudent(StudentDTO studentDTO);

    /**
     * Récupère la liste des étudiants pour une année scolaire spécifique
     * @param academicYear L'année scolaire
     * @return La liste des DTO d'étudiants
     */
    List<StudentDTO> getStudentsForCurrentYear(String academicYear);
}