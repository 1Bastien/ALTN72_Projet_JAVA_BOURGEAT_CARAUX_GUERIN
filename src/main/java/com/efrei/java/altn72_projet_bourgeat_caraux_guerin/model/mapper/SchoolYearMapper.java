package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.mapper;

import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto.SchoolYearDTO;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.SchoolYear;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper pour l'année scolaire
 */
@Mapper(componentModel = "spring")
public interface SchoolYearMapper {

    /**
     * Convertit une année scolaire en DTO
     * @param schoolYear L'année scolaire
     * @return Le DTO
     */
    SchoolYearDTO toDTO(SchoolYear schoolYear);

    /**
     * Convertit un DTO en année scolaire
     * @param schoolYearDTO Le DTO
     * @return L'année scolaire
     */
    SchoolYear toEntity(SchoolYearDTO schoolYearDTO);

    /**
     * Convertit une liste d'années scolaires en liste de DTO
     * @param schoolYears La liste d'années scolaires
     * @return La liste de DTO
     */
    List<SchoolYearDTO> toDTOList(List<SchoolYear> schoolYears);
}
