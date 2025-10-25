package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities;

import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.enums.Format;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entité pour la visite
 */
@Entity
@Getter
@Setter
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Format format;

    @Column(length = 500, nullable = true)
    private String comment;

    @ManyToOne
    @JoinColumn(name = "school_year_id", nullable = false)
    private SchoolYear schoolYear;
}