package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO pour la mise à jour des informations de base d'un étudiant
 */
@Getter
@Setter
public class StudentUpdateDTO {

    @Size(max = 50, message = "Le prénom doit contenir maximum 50 caractères")
    private String firstName;

    @Size(min = 2, max = 50, message = "Le nom doit contenir entre 2 et 50 caractères")
    @NotBlank(message = "Le nom est obligatoire")
    private String lastName;

    @Email(message = "L'email doit être valide")
    @NotBlank(message = "L'email est obligatoire")
    private String email;

    @Pattern(regexp = "^$|^0[0-9]{9}$", message = "Le téléphone doit être vide ou contenir 10 chiffres commençant par 0")
    private String phone;
}

