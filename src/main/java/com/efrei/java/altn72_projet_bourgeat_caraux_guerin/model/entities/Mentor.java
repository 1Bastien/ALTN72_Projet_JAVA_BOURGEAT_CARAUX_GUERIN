package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Entité pour le maitre d'apprentissage
 */
@Entity
@Getter
@Setter
public class Mentor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(nullable = true, length = 50)
    private String firstName;

    @Column(nullable = true, length = 100)
    private String jobTitle;

    @Column(nullable = true, length = 50, unique = true)
    private String email;

    @Column(nullable = true, length = 10)
    private String phone;

    @Column(nullable = true, length = 500)
    private String comment;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
}