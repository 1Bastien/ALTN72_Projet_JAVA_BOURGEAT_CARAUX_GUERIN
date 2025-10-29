package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO pour les critères de recherche d'étudiants
 */
@Getter
@Setter
public class SearchCriteriaDTO {
    
    @Size(max = 50, message = "Le nom doit contenir maximum 50 caractères")
    private String name;
    
    @Size(max = 100, message = "Le nom de l'entreprise doit contenir maximum 100 caractères")
    private String company;
    
    @Size(max = 100, message = "Les mots-clés doivent contenir maximum 100 caractères")
    private String missionKeyword;
    
    @Pattern(regexp = "^\\d{4}/\\d{4}$", message = "L'année académique doit être au format XXXX/XXXX")
    private String academicYear;
}

