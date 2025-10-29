package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.service;

import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto.SearchCriteriaDTO;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto.StudentDTO;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto.StudentUpdateDTO;
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

    /**
     * Crée la prochaine année académique pour tous les étudiants d'une année donnée
     * @param currentAcademicYear L'année académique actuelle
     * @return Un message de résultat avec les statistiques
     */
    String progressToNextAcademicYear(String currentAcademicYear);

    /**
     * Récupère tous les étudiants archivés
     * @return La liste des DTO d'étudiants archivés
     */
    List<StudentDTO> getAllArchivedStudents();

    /**
     * Supprime définitivement un étudiant
     * @param id L'ID de l'étudiant
     */
    void deleteStudent(Long id);

    /**
     * Désarchive un étudiant
     * @param id L'ID de l'étudiant
     */
    void unarchiveStudent(Long id);

    /**
     * Recherche des étudiants selon plusieurs critères
     * @param criteria Les critères de recherche
     * @return La liste des DTO d'étudiants correspondant aux critères
     */
    List<StudentDTO> searchStudents(SearchCriteriaDTO criteria);

    /**
     * Met à jour les informations de base d'un étudiant
     * @param studentId L'ID de l'étudiant
     * @param dto Le DTO de mise à jour
     */
    void updateStudentBasicInfo(Long studentId, StudentUpdateDTO dto);
}