package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.repository;

import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.Mentor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository pour le maitre d'apprentissage
 */
public interface MentorRepository extends JpaRepository<Mentor, Long> {
    /**
     * Récupère les mentors par entreprise
     * @param companyId L'ID de l'entreprise
     * @return La liste des mentors
     */
    List<Mentor> findByCompanyId(Long companyId);
}
