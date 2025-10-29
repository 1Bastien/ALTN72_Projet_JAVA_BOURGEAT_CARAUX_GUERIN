package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.service;

import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto.CompanyDTO;

import java.util.List;

/**
 * Service pour la gestion des entreprises
 */
public interface CompanyService {
    /**
     * Récupère toutes les entreprises
     * @return La liste des entreprises
     */
    List<CompanyDTO> getAllCompanies();

    /**
     * Récupère une entreprise par son ID
     * @param id L'ID de l'entreprise
     * @return L'entreprise
     */
    CompanyDTO getCompanyById(Long id);

    /**
     * Crée une nouvelle entreprise
     * @param companyDTO Le DTO de l'entreprise
     * @return L'entreprise créée
     */
    CompanyDTO createCompany(CompanyDTO companyDTO);

    /**
     * Met à jour une entreprise
     * @param id L'ID de l'entreprise
     * @param companyDTO Le DTO de l'entreprise
     * @return L'entreprise mise à jour
     */
    CompanyDTO updateCompany(Long id, CompanyDTO companyDTO);

    /**
     * Supprime une entreprise
     * @param id L'ID de l'entreprise
     */
    void deleteCompany(Long id);
}

