package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.mapper;

import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto.VisitDTO;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.Visit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper pour la visite
 */
@Mapper(componentModel = "spring")
public interface VisitMapper {

    /**
     * Convertit une visite en DTO
     * @param visit La visite
     * @return Le DTO
     */
    @Mapping(source = "schoolYear.id", target = "schoolYearId")
    VisitDTO toDTO(Visit visit);

    /**
     * Convertit un DTO en visite (sans la relation SchoolYear)
     * @param visitDTO Le DTO
     * @return La visite
     */
    @Mapping(target = "schoolYear", ignore = true)
    Visit toEntity(VisitDTO visitDTO);

    /**
     * Convertit une liste de visites en liste de DTO
     * @param visits La liste de visites
     * @return La liste de DTO
     */
    List<VisitDTO> toDTOList(List<Visit> visits);
}

