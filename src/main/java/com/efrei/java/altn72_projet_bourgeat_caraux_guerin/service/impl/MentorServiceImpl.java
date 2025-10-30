package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.service.impl;

import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.exception.MentorServiceException;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto.MentorDTO;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.Company;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.Mentor;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.repository.CompanyRepository;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.repository.MentorRepository;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.mapper.MentorMapper;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.service.MentorService;
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
 * Implémentation du service pour la gestion des mentors
 */
@Service
public class MentorServiceImpl implements MentorService {

    private static final Logger logger = LoggerFactory.getLogger(MentorServiceImpl.class);

    private final MentorRepository mentorRepository;
    private final CompanyRepository companyRepository;
    private final MentorMapper mentorMapper;

    public MentorServiceImpl(MentorRepository mentorRepository, 
                            CompanyRepository companyRepository,
                            MentorMapper mentorMapper) {
        this.mentorRepository = mentorRepository;
        this.companyRepository = companyRepository;
        this.mentorMapper = mentorMapper;
    }


    // Implementation d'une exception personnalisée pour les erreurs du service Mentor
    @Override
    public List<MentorDTO> getAllMentors() {
        try {
            List<Mentor> mentors = mentorRepository.findAll();
            return mentors.isEmpty() ? List.of() : mentorMapper.toDTOList(mentors);
        } catch (DataAccessException ex) {
            logger.error("Erreur d'accès aux données lors de la récupération des mentors", ex);
            throw new MentorServiceException(
                "Une erreur de base de données est survenue lors de la récupération des mentors", ex);
        }
    }

    @Override
    public List<MentorDTO> getMentorsByCompanyId(Long companyId) {
        try {
            List<Mentor> mentors = mentorRepository.findByCompanyId(companyId);
            return mentors.isEmpty() ? List.of() : mentorMapper.toDTOList(mentors);
        } catch (DataAccessException ex) {
            logger.error("Erreur d'accès aux données lors de la récupération des mentors pour l'entreprise {}", 
                companyId, ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Une erreur de base de données est survenue lors de la récupération des mentors");
        }
    }

    @Override
    public MentorDTO getMentorById(Long id) {
        return mentorMapper.toDTO(mentorRepository.findById(id)
            .orElseThrow(() -> {
                logger.error("Aucun mentor trouvé avec l'ID : {}", id);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, 
                    String.format("Le mentor avec l'identifiant %d est introuvable", id));
            }));
    }

    @Override
    @Transactional
    public MentorDTO createMentor(MentorDTO mentorDTO) {
        Company company = companyRepository.findById(mentorDTO.getCompanyId())
            .orElseThrow(() -> {
                logger.error("Aucune entreprise trouvée avec l'ID : {}", mentorDTO.getCompanyId());
                return new ResponseStatusException(HttpStatus.NOT_FOUND, 
                    String.format("L'entreprise avec l'identifiant %d est introuvable", mentorDTO.getCompanyId()));
            });
        
        try {
            Mentor mentor = mentorMapper.toEntity(mentorDTO);
            mentor.setCompany(company);
            
            Mentor savedMentor = mentorRepository.save(mentor);
            
            logger.info("Mentor {} {} créé avec succès", savedMentor.getFirstName(), savedMentor.getLastName());
            return mentorMapper.toDTO(savedMentor);
            
        } catch (DataIntegrityViolationException ex) {
            logger.error("Violation de contrainte d'unicité pour l'email : {}", mentorDTO.getEmail(), ex);
            throw new ResponseStatusException(HttpStatus.CONFLICT, 
                String.format("L'adresse email '%s' est déjà utilisée", mentorDTO.getEmail()));
        } catch (DataAccessException ex) {
            logger.error("Erreur d'accès aux données lors de la création du mentor {} {}", 
                mentorDTO.getFirstName(), mentorDTO.getLastName(), ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                "Une erreur de base de données est survenue lors de la création du mentor");
        }
    }

    @Override
    @Transactional
    public MentorDTO updateMentor(Long id, MentorDTO mentorDTO) {
        // Vérifier que le mentor existe
        if (!mentorRepository.existsById(id)) {
            logger.error("Aucun mentor trouvé avec l'ID : {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
                String.format("Le mentor avec l'identifiant %d est introuvable", id));
        }
        
        Company company = companyRepository.findById(mentorDTO.getCompanyId())
            .orElseThrow(() -> {
                logger.error("Aucune entreprise trouvée avec l'ID : {}", mentorDTO.getCompanyId());
                return new ResponseStatusException(HttpStatus.NOT_FOUND, 
                    String.format("L'entreprise avec l'identifiant %d est introuvable", mentorDTO.getCompanyId()));
            });
        
        try {
            Mentor mentor = mentorMapper.toEntity(mentorDTO);
            mentor.setId(id);
            mentor.setCompany(company);
            
            Mentor updatedMentor = mentorRepository.save(mentor);
            
            logger.info("Mentor {} mis à jour avec succès", id);
            return mentorMapper.toDTO(updatedMentor);
            
        } catch (DataIntegrityViolationException ex) {
            logger.error("Violation de contrainte d'unicité pour l'email : {}", mentorDTO.getEmail(), ex);
            throw new ResponseStatusException(HttpStatus.CONFLICT, 
                String.format("L'adresse email '%s' est déjà utilisée", mentorDTO.getEmail()));
        } catch (DataAccessException ex) {
            logger.error("Erreur d'accès aux données lors de la mise à jour du mentor {}", id, ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                "Une erreur de base de données est survenue lors de la mise à jour du mentor");
        }
    }

    @Override
    @Transactional
    public void deleteMentor(Long id) {
        Mentor mentor = mentorRepository.findById(id)
            .orElseThrow(() -> {
                logger.error("Aucun mentor trouvé avec l'ID : {}", id);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, 
                    String.format("Le mentor avec l'identifiant %d est introuvable", id));
            });
        
        try {
            mentorRepository.delete(mentor);
            logger.info("Mentor {} supprimé avec succès", id);
            
        } catch (DataIntegrityViolationException ex) {
            logger.error("Impossible de supprimer le mentor {} car il est référencé", id, ex);
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                "Impossible de supprimer le mentor car il est associé à des étudiants");
        } catch (DataAccessException ex) {
            logger.error("Erreur d'accès aux données lors de la suppression du mentor {}", id, ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                "Une erreur de base de données est survenue lors de la suppression du mentor");
        }
    }
}

