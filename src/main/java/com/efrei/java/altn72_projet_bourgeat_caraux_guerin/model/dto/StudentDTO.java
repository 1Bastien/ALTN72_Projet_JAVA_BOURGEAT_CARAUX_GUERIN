package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO pour l'étudiant
 */
@Data
public class StudentDTO {

    @Size(max = 50, message = "Le prénom doit contenir maximum 50 caractères")
    private String firstName;

    @Size(min = 2, max = 50, message = "Le nom doit contenir entre 2 et 50 caractères")
    @NotBlank(message = "Le nom est obligatoire")
    private String lastName;

    @Email(message = "L'email doit être valide")
    @NotBlank(message = "L'email est obligatoire")
    private String email;

    @Pattern(regexp = "^$|^\\d{10}$", message = "Le numéro de téléphone doit contenir exactement 10 chiffres")
    private String phone;

    @NotNull(message = "Les informations de l'année scolaire sont obligatoires")
    private SchoolYearDTO schoolYear;
}
