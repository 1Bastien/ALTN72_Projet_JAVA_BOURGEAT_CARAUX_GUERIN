package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Entité pour l'étudiant
 */
@Entity
@Getter
@Setter
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(nullable = true, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50, unique = true)
    private String email;

    @Column(nullable = true, length = 10)
    private String phone;

    @Column(nullable = true, length = 10)
    private Boolean isArchived;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SchoolYear> schoolYears;
}
