package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String companyName;

    @Column(nullable = false, length = 200)
    private String address;

    @Column(nullable = true, length = 500)
    private String accessInformation;

    @OneToMany(mappedBy = "company")
    private List<Mentor> mentors;

    @OneToMany(mappedBy = "company")
    private List<SchoolYear> schoolYears;
}
