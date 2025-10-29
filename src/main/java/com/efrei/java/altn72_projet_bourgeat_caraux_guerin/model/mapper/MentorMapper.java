package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.mapper;

import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto.MentorDTO;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.Mentor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper pour le maître d'apprentissage
 */
@Mapper(componentModel = "spring")
public interface MentorMapper {

    /**
     * Convertit un mentor en DTO
     * @param mentor Le mentor
     * @return Le DTO
     */
    @Mapping(source = "company.id", target = "companyId")
    @Mapping(source = "company.companyName", target = "companyName")
    MentorDTO toDTO(Mentor mentor);

    /**
     * Convertit un DTO en mentor
     * @param mentorDTO Le DTO
     * @return Le mentor
     */
    @Mapping(target = "company", ignore = true)
    Mentor toEntity(MentorDTO mentorDTO);

    /**
     * Convertit une liste de mentors en liste de DTO
     * @param mentors La liste de mentors
     * @return La liste de DTO
     */
    List<MentorDTO> toDTOList(List<Mentor> mentors);
}

