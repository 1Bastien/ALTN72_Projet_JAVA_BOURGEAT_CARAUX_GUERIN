package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Classe embeddable pour les informations du rapport
 */
@Embeddable
@Getter
@Setter
public class Report {
    @Column(nullable = true, length = 100)
    private String subject;

    @Column(nullable = true, precision = 5, scale = 2)
    private BigDecimal reportGrade;

    @Column(name = "report_comment", nullable = true, length = 500)
    private String comment;
}
