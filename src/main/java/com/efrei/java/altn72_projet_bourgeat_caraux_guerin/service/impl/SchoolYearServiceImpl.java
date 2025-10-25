package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.service.impl;

import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.repository.SchoolYearRepository;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.service.SchoolYearService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SchoolYearServiceImpl implements SchoolYearService {

    private static final Logger logger = LoggerFactory.getLogger(SchoolYearServiceImpl.class);

    private final SchoolYearRepository schoolYearRepository;

    public SchoolYearServiceImpl(SchoolYearRepository schoolYearRepository) {
        this.schoolYearRepository = schoolYearRepository;
    }

    @Override
    public List<String> getAllAcademicYears() {
        try {
            List<String> years = schoolYearRepository.findDistinctAcademicYearByOrderByAcademicYearDesc();
            logger.info("Années académiques récupérées : {}", years);
            return years;
        } catch (Exception ex) {
            logger.error("Erreur lors de la récupération des années académiques", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Une erreur est survenue lors de la récupération des années académiques");
        }
    }
}
