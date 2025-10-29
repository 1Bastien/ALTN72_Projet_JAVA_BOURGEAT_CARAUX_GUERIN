package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.mapper;

import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto.CompanyDTO;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.Company;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper pour l'entreprise
 */
@Mapper(componentModel = "spring")
public interface CompanyMapper {

    /**
     * Convertit une entreprise en DTO
     * @param company L'entreprise
     * @return Le DTO
     */
    CompanyDTO toDTO(Company company);

    /**
     * Convertit un DTO en entreprise
     * @param companyDTO Le DTO
     * @return L'entreprise
     */
    @Mapping(target = "mentors", ignore = true)
    @Mapping(target = "schoolYears", ignore = true)
    Company toEntity(CompanyDTO companyDTO);

    /**
     * Convertit une liste d'entreprises en liste de DTO
     * @param companies La liste d'entreprises
     * @return La liste de DTO
     */
    List<CompanyDTO> toDTOList(List<Company> companies);
}

