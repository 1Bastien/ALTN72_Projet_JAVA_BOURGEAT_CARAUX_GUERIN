package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.service;

import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto.MentorDTO;

import java.util.List;

/**
 * Service pour la gestion des mentors
 */
public interface MentorService {
    /**
     * Récupère tous les mentors
     * @return La liste des mentors
     */
    List<MentorDTO> getAllMentors();

    /**
     * Récupère les mentors par entreprise
     * @param companyId L'ID de l'entreprise
     * @return La liste des mentors
     */
    List<MentorDTO> getMentorsByCompanyId(Long companyId);

    /**
     * Récupère un mentor par son ID
     * @param id L'ID du mentor
     * @return Le mentor
     */
    MentorDTO getMentorById(Long id);

    /**
     * Crée un nouveau mentor
     * @param mentorDTO Le DTO du mentor
     * @return Le mentor créé
     */
    MentorDTO createMentor(MentorDTO mentorDTO);

    /**
     * Met à jour un mentor
     * @param id L'ID du mentor
     * @param mentorDTO Le DTO du mentor
     * @return Le mentor mis à jour
     */
    MentorDTO updateMentor(Long id, MentorDTO mentorDTO);

    /**
     * Supprime un mentor
     * @param id L'ID du mentor
     */
    void deleteMentor(Long id);
}

