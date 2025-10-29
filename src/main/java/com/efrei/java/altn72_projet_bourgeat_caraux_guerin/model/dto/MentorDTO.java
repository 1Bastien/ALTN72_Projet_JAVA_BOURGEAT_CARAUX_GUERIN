package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO pour le maître d'apprentissage
 */
@Getter
@Setter
public class MentorDTO {

    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 50, message = "Le nom doit contenir maximum 50 caractères")
    private String lastName;

    @Size(max = 50, message = "Le prénom doit contenir maximum 50 caractères")
    private String firstName;

    @Size(max = 100, message = "Le poste doit contenir maximum 100 caractères")
    private String jobTitle;

    @Email(message = "L'email doit être valide")
    @Size(max = 50, message = "L'email doit contenir maximum 50 caractères")
    private String email;

    @Pattern(regexp = "^$|^0[0-9]{9}$", message = "Le téléphone doit être vide ou contenir 10 chiffres commençant par 0")
    private String phone;

    @Size(max = 500, message = "Le commentaire doit contenir maximum 500 caractères")
    private String comment;

    @NotNull(message = "L'ID de l'entreprise est obligatoire")
    private Long companyId;
    
    private String companyName;
}

