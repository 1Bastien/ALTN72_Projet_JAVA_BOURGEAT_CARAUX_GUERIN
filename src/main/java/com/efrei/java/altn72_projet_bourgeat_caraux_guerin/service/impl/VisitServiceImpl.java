package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.service.impl;

import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto.VisitDTO;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.SchoolYear;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.Visit;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.repository.SchoolYearRepository;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.repository.VisitRepository;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.mapper.VisitMapper;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.service.VisitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Implémentation du service pour la gestion des visites
 */
@Service
public class VisitServiceImpl implements VisitService {

    private static final Logger logger = LoggerFactory.getLogger(VisitServiceImpl.class);

    private final VisitRepository visitRepository;
    private final SchoolYearRepository schoolYearRepository;
    private final VisitMapper visitMapper;

    public VisitServiceImpl(VisitRepository visitRepository,
                           SchoolYearRepository schoolYearRepository,
                           VisitMapper visitMapper) {
        this.visitRepository = visitRepository;
        this.schoolYearRepository = schoolYearRepository;
        this.visitMapper = visitMapper;
    }

    @Override
    public List<VisitDTO> getVisitsBySchoolYearId(Long schoolYearId) {
        try {
            List<Visit> visits = visitRepository.findBySchoolYearId(schoolYearId);
            return visits.isEmpty() ? List.of() : visitMapper.toDTOList(visits);
        } catch (DataAccessException ex) {
            logger.error("Erreur d'accès aux données lors de la récupération des visites pour l'année scolaire {}", 
                schoolYearId, ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Une erreur de base de données est survenue lors de la récupération des visites");
        }
    }

    @Override
    public VisitDTO getVisitById(Long id) {
        return visitMapper.toDTO(visitRepository.findById(id)
            .orElseThrow(() -> {
                logger.error("Aucune visite trouvée avec l'ID : {}", id);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, 
                    String.format("La visite avec l'identifiant %d est introuvable", id));
            }));
    }

    @Override
    @Transactional
    public VisitDTO createVisit(VisitDTO visitDTO) {
        SchoolYear schoolYear = schoolYearRepository.findById(visitDTO.getSchoolYearId())
            .orElseThrow(() -> {
                logger.error("Aucune année scolaire trouvée avec l'ID : {}", visitDTO.getSchoolYearId());
                return new ResponseStatusException(HttpStatus.NOT_FOUND, 
                    String.format("L'année scolaire avec l'identifiant %d est introuvable", visitDTO.getSchoolYearId()));
            });

        try {
            Visit visit = visitMapper.toEntity(visitDTO);
            visit.setSchoolYear(schoolYear);

            Visit savedVisit = visitRepository.save(visit);

            logger.info("Visite créée avec succès pour l'année scolaire {}", visitDTO.getSchoolYearId());
            return visitMapper.toDTO(savedVisit);

        } catch (DataAccessException ex) {
            logger.error("Erreur d'accès aux données lors de la création de la visite", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                "Une erreur de base de données est survenue lors de la création de la visite");
        }
    }

    @Override
    @Transactional
    public VisitDTO updateVisit(Long id, VisitDTO visitDTO) {
        Visit visit = visitRepository.findById(id)
            .orElseThrow(() -> {
                logger.error("Aucune visite trouvée avec l'ID : {}", id);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, 
                    String.format("La visite avec l'identifiant %d est introuvable", id));
            });

        SchoolYear schoolYear = schoolYearRepository.findById(visitDTO.getSchoolYearId())
            .orElseThrow(() -> {
                logger.error("Aucune année scolaire trouvée avec l'ID : {}", visitDTO.getSchoolYearId());
                return new ResponseStatusException(HttpStatus.NOT_FOUND, 
                    String.format("L'année scolaire avec l'identifiant %d est introuvable", visitDTO.getSchoolYearId()));
            });

        try {
            visit.setDate(visitDTO.getDate());
            visit.setFormat(visitDTO.getFormat());
            visit.setComment(visitDTO.getComment());
            visit.setSchoolYear(schoolYear);

            Visit updatedVisit = visitRepository.save(visit);

            logger.info("Visite {} mise à jour avec succès", id);
            return visitMapper.toDTO(updatedVisit);

        } catch (DataAccessException ex) {
            logger.error("Erreur d'accès aux données lors de la mise à jour de la visite {}", id, ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                "Une erreur de base de données est survenue lors de la mise à jour de la visite");
        }
    }

    @Override
    @Transactional
    public void deleteVisit(Long id) {
        Visit visit = visitRepository.findById(id)
            .orElseThrow(() -> {
                logger.error("Aucune visite trouvée avec l'ID : {}", id);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, 
                    String.format("La visite avec l'identifiant %d est introuvable", id));
            });

        try {
            visitRepository.delete(visit);
            logger.info("Visite {} supprimée avec succès", id);

        } catch (DataAccessException ex) {
            logger.error("Erreur d'accès aux données lors de la suppression de la visite {}", id, ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                "Une erreur de base de données est survenue lors de la suppression de la visite");
        }
    }
}

