package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities;

import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.enums.Program;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Getter
@Setter
public class SchoolYear {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true, length = 100)
    @Enumerated(EnumType.STRING)
    private Program program;

    @Column(nullable = false, length = 10)
    private String academicYear;

    @Column(nullable = true, length = 100)
    private String major;

    @Column(nullable = true, length = 500)
    private String comment;

    @Column(nullable = true, length = 500)
    private String feedback;

    @Embedded
    private Mission mission;

    @Embedded
    private Report report;

    @Embedded
    private Presentation presentation;

    @ManyToOne
    @JoinColumn(name = "mentor_id", nullable = false)
    private Mentor mentor;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @OneToMany(mappedBy = "schoolYear", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Visit> visits;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
}
