package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Classe embeddable pour les informations de la présentation,
 * cette classe sera dans la classe SchoolYear dans la base de données.
 */
@Embeddable
@Getter
@Setter
public class Presentation {
    @Column(nullable = true)
    private LocalDateTime date;

    @Column(nullable = true, precision = 5, scale = 2)
    private BigDecimal presentationGrade;

    @Column(name = "presentation_comment", nullable = true, length = 500)
    private String comment;
}
