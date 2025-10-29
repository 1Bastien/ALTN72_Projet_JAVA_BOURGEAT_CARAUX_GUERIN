package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.controler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Contrôleur pour l'authentification
 */
@Controller
@Tag(name = "Authentification", description = "Gestion de l'authentification des utilisateurs")
public class AuthController {

    /**
     * Affiche la page de connexion
     * @return Le nom de la vue
     */
    @Operation(summary = "Afficher la page de connexion", description = "Affiche la page de connexion de l'application")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Page de connexion affichée avec succès")
    })
    @GetMapping("/login-page")
    public String login() {
        return "login";
    }
}

