package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto;

import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.enums.Program;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * DTO pour l'année scolaire
 */
@Data
public class SchoolYearDTO {

    @NotBlank(message = "L'année scolaire est obligatoire")
    @Pattern(regexp = "^\\d{4}/\\d{4}$", message = "L'année scolaire doit être au format XXXX/XXXX")
    private String academicYear;

    @NotNull(message = "Le programme est obligatoire")
    private Program program;
}
