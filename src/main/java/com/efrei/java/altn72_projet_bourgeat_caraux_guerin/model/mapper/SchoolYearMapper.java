package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.mapper;

import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto.SchoolYearDTO;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto.SchoolYearUpdateDTO;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.SchoolYear;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

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
    @Mapping(source = "mission.keywords", target = "missionKeywords")
    @Mapping(source = "mission.targetJob", target = "missionTargetJob")
    @Mapping(source = "mission.comment", target = "missionComment")
    @Mapping(source = "company.id", target = "companyId")
    @Mapping(source = "mentor.id", target = "mentorId")
    @Mapping(source = "student.id", target = "studentId")
    SchoolYearDTO toDTO(SchoolYear schoolYear);

    /**
     * Convertit un DTO en année scolaire
     * @param schoolYearDTO Le DTO
     * @return L'année scolaire
     */
    @Mapping(target = "mission", ignore = true)
    @Mapping(target = "report", ignore = true)
    @Mapping(target = "presentation", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "mentor", ignore = true)
    @Mapping(target = "visits", ignore = true)
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "id", ignore = true)
    SchoolYear toEntity(SchoolYearDTO schoolYearDTO);

    /**
     * Convertit une liste d'années scolaires en liste de DTO
     * @param schoolYears La liste d'années scolaires
     * @return La liste de DTO
     */
    List<SchoolYearDTO> toDTOList(List<SchoolYear> schoolYears);

    /**
     * Met à jour une année scolaire existante à partir d'un SchoolYearUpdateDTO
     * Les valeurs nulles du DTO ne modifient pas l'entité
     * @param dto Le DTO de mise à jour
     * @param schoolYear L'année scolaire à mettre à jour
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "academicYear", ignore = true)
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "mentor", ignore = true)
    @Mapping(target = "visits", ignore = true)
    @Mapping(target = "report", ignore = true)
    @Mapping(target = "presentation", ignore = true)
    @Mapping(target = "mission", ignore = true)
    @Mapping(source = "program", target = "program", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "major", target = "major", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "comment", target = "comment", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "feedback", target = "feedback", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDTO(SchoolYearUpdateDTO dto, @MappingTarget SchoolYear schoolYear);
}
