package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto;

import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.enums.Program;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO pour la mise à jour d'une année scolaire
 */
@Getter
@Setter
public class SchoolYearUpdateDTO {

    private Long companyId;
    
    private Long mentorId;
    
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
    
    @Size(max = 100, message = "Le sujet du rapport doit contenir maximum 100 caractères")
    private String reportSubject;
    
    @DecimalMin(value = "0.0", message = "La note du rapport doit être supérieure ou égale à 0")
    @DecimalMax(value = "20.0", message = "La note du rapport doit être inférieure ou égale à 20")
    private BigDecimal reportGrade;
    
    @Size(max = 500, message = "Le commentaire du rapport doit contenir maximum 500 caractères")
    private String reportComment;
    
    private LocalDate presentationDate;
    
    private LocalTime presentationTime;
    
    @DecimalMin(value = "0.0", message = "La note de présentation doit être supérieure ou égale à 0")
    @DecimalMax(value = "20.0", message = "La note de présentation doit être inférieure ou égale à 20")
    private BigDecimal presentationGrade;
    
    @Size(max = 500, message = "Le commentaire de présentation doit contenir maximum 500 caractères")
    private String presentationComment;
}

