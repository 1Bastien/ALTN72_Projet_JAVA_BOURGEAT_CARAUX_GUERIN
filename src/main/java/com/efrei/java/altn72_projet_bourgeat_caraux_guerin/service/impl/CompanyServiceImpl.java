package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.service.impl;

import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto.CompanyDTO;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.Company;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.repository.CompanyRepository;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.mapper.CompanyMapper;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.service.CompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Implémentation du service pour la gestion des entreprises
 */
@Service
public class CompanyServiceImpl implements CompanyService {

    private static final Logger logger = LoggerFactory.getLogger(CompanyServiceImpl.class);

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    public CompanyServiceImpl(CompanyRepository companyRepository, CompanyMapper companyMapper) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
    }

    @Override
    public List<CompanyDTO> getAllCompanies() {
        try {
            List<Company> companies = companyRepository.findAll();
            return companies.isEmpty() ? List.of() : companyMapper.toDTOList(companies);
        } catch (DataAccessException ex) {
            logger.error("Erreur d'accès aux données lors de la récupération des entreprises", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Une erreur de base de données est survenue lors de la récupération des entreprises");
        }
    }

    @Override
    public CompanyDTO getCompanyById(Long id) {
        return companyMapper.toDTO(companyRepository.findById(id)
            .orElseThrow(() -> {
                logger.error("Aucune entreprise trouvée avec l'ID : {}", id);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, 
                    String.format("L'entreprise avec l'identifiant %d est introuvable", id));
            }));
    }

    @Override
    @Transactional
    public CompanyDTO createCompany(CompanyDTO companyDTO) {
        try {
            Company company = companyMapper.toEntity(companyDTO);
            Company savedCompany = companyRepository.save(company);
            
            logger.info("Entreprise {} créée avec succès", savedCompany.getCompanyName());
            return companyMapper.toDTO(savedCompany);
            
        } catch (DataIntegrityViolationException ex) {
            logger.error("Violation de contrainte d'intégrité lors de la création de l'entreprise {}", 
                companyDTO.getCompanyName(), ex);
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                "Une entreprise avec ce nom existe déjà");
        } catch (DataAccessException ex) {
            logger.error("Erreur d'accès aux données lors de la création de l'entreprise {}", 
                companyDTO.getCompanyName(), ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                "Une erreur de base de données est survenue lors de la création de l'entreprise");
        }
    }

    @Override
    @Transactional
    public CompanyDTO updateCompany(Long id, CompanyDTO companyDTO) {
        Company company = companyRepository.findById(id)
            .orElseThrow(() -> {
                logger.error("Aucune entreprise trouvée avec l'ID : {}", id);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, 
                    String.format("L'entreprise avec l'identifiant %d est introuvable", id));
            });
        
        try {
            company.setCompanyName(companyDTO.getCompanyName());
            company.setAddress(companyDTO.getAddress());
            company.setAccessInformation(companyDTO.getAccessInformation());
            
            Company updatedCompany = companyRepository.save(company);
            
            logger.info("Entreprise {} mise à jour avec succès", id);
            return companyMapper.toDTO(updatedCompany);
            
        } catch (DataIntegrityViolationException ex) {
            logger.error("Violation de contrainte d'intégrité lors de la mise à jour de l'entreprise {}", id, ex);
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                "Une entreprise avec ce nom existe déjà");
        } catch (DataAccessException ex) {
            logger.error("Erreur d'accès aux données lors de la mise à jour de l'entreprise {}", id, ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                "Une erreur de base de données est survenue lors de la mise à jour de l'entreprise");
        }
    }

    @Override
    @Transactional
    public void deleteCompany(Long id) {
        Company company = companyRepository.findById(id)
            .orElseThrow(() -> {
                logger.error("Aucune entreprise trouvée avec l'ID : {}", id);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, 
                    String.format("L'entreprise avec l'identifiant %d est introuvable", id));
            });
        
        try {
            companyRepository.delete(company);
            logger.info("Entreprise {} supprimée avec succès", id);
            
        } catch (DataIntegrityViolationException ex) {
            logger.error("Impossible de supprimer l'entreprise {} car elle est référencée", id, ex);
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                "Impossible de supprimer l'entreprise car elle est associée à des mentors ou des années scolaires");
        } catch (DataAccessException ex) {
            logger.error("Erreur d'accès aux données lors de la suppression de l'entreprise {}", id, ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                "Une erreur de base de données est survenue lors de la suppression de l'entreprise");
        }
    }
}

