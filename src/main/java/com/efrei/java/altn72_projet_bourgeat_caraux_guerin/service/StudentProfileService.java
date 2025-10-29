package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.service;

import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto.SchoolYearUpdateDTO;
import org.springframework.ui.Model;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.Company;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.Mentor;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.SchoolYear;

/**
 * Service pour la gestion de la page de profil d'un étudiant
 */
public interface StudentProfileService {
    /**
     * Prépare le modèle pour la page de profil
     * @param model Le modèle
     * @param studentId L'ID de l'étudiant
     * @param academicYear L'année académique sélectionnée
     */
    void prepareProfileModel(Model model, Long studentId, String academicYear);

    /**
     * Met à jour les informations d'une année scolaire
     * @param schoolYearId L'ID de l'année scolaire à mettre à jour
     * @param dto Le DTO contenant les informations à mettre à jour
     * @return L'année scolaire mise à jour
     */
    SchoolYear updateSchoolYear(Long schoolYearId, SchoolYearUpdateDTO dto);

    /**
     * Récupère l'année scolaire d'un étudiant pour une année académique donnée
     * @param studentId L'ID de l'étudiant
     * @param academicYear L'année académique
     * @return L'année scolaire
     */
    SchoolYear getSchoolYearByStudentIdAndAcademicYear(Long studentId, String academicYear);

    /**
     * Récupère une année scolaire par son ID
     * @param schoolYearId L'ID de l'année scolaire
     * @return L'année scolaire
     */
    SchoolYear getSchoolYearById(Long schoolYearId);

    /**
     * Récupère une entreprise en tant qu'entité par son ID
     * @param companyId L'ID de l'entreprise
     * @return L'entreprise
     */
    Company getCompanyEntityById(Long companyId);

    /**
     * Récupère un mentor en tant qu'entité par son ID
     * @param mentorId L'ID du mentor
     * @return Le mentor
     */
    Mentor getMentorEntityById(Long mentorId);
}

