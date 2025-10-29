package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto;

import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.enums.Program;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO pour l'année scolaire
 */
@Getter
@Setter
public class SchoolYearDTO {

    private Long id;

    @NotBlank(message = "L'année scolaire est obligatoire")
    @Pattern(regexp = "^\\d{4}/\\d{4}$", message = "L'année scolaire doit être au format XXXX/XXXX")
    private String academicYear;

    @NotNull(message = "Le programme est obligatoire")
    private Program program;

    @Size(max = 100, message = "La spécialité doit contenir maximum 100 caractères")
    private String major;

    @Size(max = 500, message = "Le commentaire doit contenir maximum 500 caractères")
    private String comment;

    @Size(max = 500, message = "Le feedback doit contenir maximum 500 caractères")
    private String feedback;

    @Size(max = 100, message = "Les mots-clés doivent contenir maximum 100 caractères")
    private String missionKeywords;

    @Size(max = 100, message = "Le métier visé doit contenir maximum 100 caractères")
    private String missionTargetJob;

    @Size(max = 500, message = "Le commentaire de mission doit contenir maximum 500 caractères")
    private String missionComment;

    private Long companyId;

    private Long mentorId;
    
    private Long studentId;
}
