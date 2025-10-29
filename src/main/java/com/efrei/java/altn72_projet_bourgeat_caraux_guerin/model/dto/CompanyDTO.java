package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO pour l'entreprise
 */
@Getter
@Setter
public class CompanyDTO {

    private Long id;

    @NotBlank(message = "Le nom de l'entreprise est obligatoire")
    @Size(max = 100, message = "Le nom de l'entreprise doit contenir maximum 100 caractères")
    private String companyName;

    @Size(max = 200, message = "L'adresse doit contenir maximum 200 caractères")
    private String address;

    @Size(max = 500, message = "Les informations d'accès doivent contenir maximum 500 caractères")
    private String accessInformation;
}

