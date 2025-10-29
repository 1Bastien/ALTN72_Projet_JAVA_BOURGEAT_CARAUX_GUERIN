package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto;

import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.enums.Format;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * DTO pour la visite
 */ 
@Getter
@Setter
public class VisitDTO {

    private Long id;

    @NotNull(message = "La date de la visite est obligatoire")
    private LocalDateTime date;

    @NotNull(message = "Le format de la visite est obligatoire")
    private Format format;

    @Size(max = 500, message = "Le commentaire doit contenir maximum 500 caractères")
    private String comment;

    @NotNull(message = "L'ID de l'année scolaire est obligatoire")
    private Long schoolYearId;
}

