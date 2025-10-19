package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Mission {
    @Column(nullable = true, length = 100)
    private String keywords;

    @Column(nullable = true, length = 100)
    private String targetJob;

    @Column(name = "mission_comment", nullable = true, length = 500)
    private String comment;
}
