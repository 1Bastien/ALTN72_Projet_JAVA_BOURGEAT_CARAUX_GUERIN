package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.controler;

import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto.CompanyDTO;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto.MentorDTO;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.service.CompanyService;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.service.MentorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Contrôleur pour la gestion des entreprises et des maîtres d'apprentissage
 */
@Controller
@RequestMapping("/management")
@Tag(name = "Gestion", description = "Gestion des entreprises et des maîtres d'apprentissage")
public class ManagementController {

    private static final Logger logger = LoggerFactory.getLogger(ManagementController.class);

    private final CompanyService companyService;
    private final MentorService mentorService;

    public ManagementController(CompanyService companyService, MentorService mentorService) {
        this.companyService = companyService;
        this.mentorService = mentorService;
    }

    /**
     * Affiche la page de gestion des entreprises
     * @param model Le modèle
     * @return Le nom de la vue
     */
    @Operation(summary = "Afficher les entreprises", description = "Affiche la page de gestion de toutes les entreprises")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Page de gestion des entreprises affichée avec succès"),
            @ApiResponse(responseCode = "302", description = "Erreur, redirection vers la page d'accueil")
    })
    @GetMapping("/companies")
    public String getCompaniesManagement(Model model) {
        try {
            model.addAttribute("companies", companyService.getAllCompanies());
            model.addAttribute("isDashboard", false);
            logger.info("Page de gestion des entreprises affichée");
            return "management-companies";
        } catch (ResponseStatusException ex) {
            logger.error("Erreur lors de l'affichage de la gestion des entreprises", ex);
            return "redirect:/";
        }
    }

    /**
     * Affiche la page de gestion des maîtres d'apprentissage
     * @param model Le modèle
     * @return Le nom de la vue
     */
    @Operation(summary = "Afficher les mentors", description = "Affiche la page de gestion de tous les maîtres d'apprentissage")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Page de gestion des mentors affichée avec succès"),
            @ApiResponse(responseCode = "302", description = "Erreur, redirection vers la page d'accueil")
    })
    @GetMapping("/mentors")
    public String getMentorsManagement(Model model) {
        try {
            model.addAttribute("mentors", mentorService.getAllMentors());
            model.addAttribute("companies", companyService.getAllCompanies());
            model.addAttribute("isDashboard", false);
            logger.info("Page de gestion des maîtres d'apprentissage affichée");
            return "management-mentors";
        } catch (ResponseStatusException ex) {
            logger.error("Erreur lors de l'affichage de la gestion des maîtres d'apprentissage", ex);
            return "redirect:/";
        }
    }

    /**
     * Crée une nouvelle entreprise
     * @param companyDTO Le DTO de l'entreprise
     * @param result Les résultats de la validation
     * @param redirectAttributes Les attributs de redirection
     * @return La redirection
     */
    @Operation(summary = "Créer une entreprise", description = "Crée une nouvelle entreprise dans le système")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Entreprise créée avec succès"),
            @ApiResponse(responseCode = "400", description = "Erreur de validation des données")
    })
    @PostMapping("/companies/create")
    public String createCompany(
            @Parameter(description = "Informations de l'entreprise à créer", required = true)
            @Valid @ModelAttribute CompanyDTO companyDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            logger.error("Erreurs de validation détectées : {}", result.getAllErrors());
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Erreur de validation : veuillez vérifier les champs obligatoires");
            return "redirect:/management/companies";
        }

        try {
            companyService.createCompany(companyDTO);
            
            redirectAttributes.addFlashAttribute("successMessage", 
                "L'entreprise a été créée avec succès");
            logger.info("Entreprise {} créée avec succès", companyDTO.getCompanyName());
            
        } catch (ResponseStatusException ex) {
            logger.error("Erreur lors de la création de l'entreprise {}", companyDTO.getCompanyName(), ex);
            redirectAttributes.addFlashAttribute("errorMessage", ex.getReason());
        }
        
        return "redirect:/management/companies";
    }

    /**
     * Met à jour une entreprise
     * @param id L'ID de l'entreprise
     * @param companyDTO Le DTO de l'entreprise
     * @param result Les résultats de la validation
     * @param redirectAttributes Les attributs de redirection
     * @return La redirection
     */
    @Operation(summary = "Mettre à jour une entreprise", description = "Met à jour les informations d'une entreprise existante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Entreprise mise à jour avec succès"),
            @ApiResponse(responseCode = "400", description = "Erreur de validation des données"),
            @ApiResponse(responseCode = "404", description = "Entreprise non trouvée")
    })
    @PostMapping("/companies/{id}/update")
    public String updateCompany(
            @Parameter(description = "ID de l'entreprise à mettre à jour", required = true)
            @PathVariable Long id,
            @Parameter(description = "Nouvelles informations de l'entreprise", required = true)
            @Valid @ModelAttribute CompanyDTO companyDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            logger.error("Erreurs de validation détectées : {}", result.getAllErrors());
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Erreur de validation : veuillez vérifier les champs obligatoires");
            return "redirect:/management/companies";
        }

        try {
            companyService.updateCompany(id, companyDTO);
            
            redirectAttributes.addFlashAttribute("successMessage", 
                "L'entreprise a été mise à jour avec succès");
            logger.info("Entreprise {} mise à jour avec succès", id);
            
        } catch (ResponseStatusException ex) {
            logger.error("Erreur lors de la mise à jour de l'entreprise {}", id, ex);
            redirectAttributes.addFlashAttribute("errorMessage", ex.getReason());
        }
        
        return "redirect:/management/companies";
    }

    /**
     * Supprime une entreprise
     * @param id L'ID de l'entreprise
     * @param redirectAttributes Les attributs de redirection
     * @return La redirection
     */
    @Operation(summary = "Supprimer une entreprise", description = "Supprime une entreprise et toutes ses dépendances")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Entreprise supprimée avec succès"),
            @ApiResponse(responseCode = "404", description = "Entreprise non trouvée"),
            @ApiResponse(responseCode = "409", description = "Impossible de supprimer, entreprise associée à des mentors ou étudiants")
    })
    @PostMapping("/companies/{id}/delete")
    public String deleteCompany(
            @Parameter(description = "ID de l'entreprise à supprimer", required = true)
            @PathVariable Long id, 
            RedirectAttributes redirectAttributes) {
        try {
            companyService.deleteCompany(id);
            
            redirectAttributes.addFlashAttribute("successMessage", 
                "L'entreprise et toutes ses dépendances ont été supprimées avec succès");
            logger.info("Entreprise {} supprimée avec succès", id);
            
        } catch (ResponseStatusException ex) {
            logger.error("Erreur lors de la suppression de l'entreprise {}", id, ex);
            redirectAttributes.addFlashAttribute("errorMessage", ex.getReason());
        }
        
        return "redirect:/management/companies";
    }

    /**
     * Crée un nouveau maître d'apprentissage
     * @param mentorDTO Le DTO du mentor
     * @param result Les résultats de la validation
     * @param redirectAttributes Les attributs de redirection
     * @return La redirection
     */
    @Operation(summary = "Créer un mentor", description = "Crée un nouveau maître d'apprentissage dans le système")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Mentor créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Erreur de validation des données")
    })
    @PostMapping("/mentors/create")
    public String createMentor(
            @Parameter(description = "Informations du mentor à créer", required = true)
            @Valid @ModelAttribute MentorDTO mentorDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            logger.error("Erreurs de validation détectées : {}", result.getAllErrors());
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Erreur de validation : veuillez vérifier les champs obligatoires");
            return "redirect:/management/mentors";
        }

        try {
            mentorService.createMentor(mentorDTO);
            
            redirectAttributes.addFlashAttribute("successMessage", 
                "Le maître d'apprentissage a été créé avec succès");
            logger.info("Maître d'apprentissage {} {} créé avec succès", 
                mentorDTO.getFirstName(), mentorDTO.getLastName());
            
        } catch (ResponseStatusException ex) {
            logger.error("Erreur lors de la création du maître d'apprentissage {} {}", 
                mentorDTO.getFirstName(), mentorDTO.getLastName(), ex);
            redirectAttributes.addFlashAttribute("errorMessage", ex.getReason());
        }
        
        return "redirect:/management/mentors";
    }

    /**
     * Met à jour un maître d'apprentissage
     * @param id L'ID du maître d'apprentissage
     * @param mentorDTO Le DTO du mentor
     * @param result Les résultats de la validation
     * @param redirectAttributes Les attributs de redirection
     * @return La redirection
     */
    @Operation(summary = "Mettre à jour un mentor", description = "Met à jour les informations d'un maître d'apprentissage existant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Mentor mis à jour avec succès"),
            @ApiResponse(responseCode = "400", description = "Erreur de validation des données"),
            @ApiResponse(responseCode = "404", description = "Mentor non trouvé")
    })
    @PostMapping("/mentors/{id}/update")
    public String updateMentor(
            @Parameter(description = "ID du mentor à mettre à jour", required = true)
            @PathVariable Long id,
            @Parameter(description = "Nouvelles informations du mentor", required = true)
            @Valid @ModelAttribute MentorDTO mentorDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            logger.error("Erreurs de validation détectées : {}", result.getAllErrors());
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Erreur de validation : veuillez vérifier les champs obligatoires");
            return "redirect:/management/mentors";
        }

        try {
            mentorService.updateMentor(id, mentorDTO);
            
            redirectAttributes.addFlashAttribute("successMessage", 
                "Le maître d'apprentissage a été mis à jour avec succès");
            logger.info("Maître d'apprentissage {} mis à jour avec succès", id);
            
        } catch (ResponseStatusException ex) {
            logger.error("Erreur lors de la mise à jour du maître d'apprentissage {}", id, ex);
            redirectAttributes.addFlashAttribute("errorMessage", ex.getReason());
        }
        
        return "redirect:/management/mentors";
    }

    /**
     * Supprime un maître d'apprentissage
     * @param id L'ID du maître d'apprentissage
     * @param redirectAttributes Les attributs de redirection
     * @return La redirection
     */
    @Operation(summary = "Supprimer un mentor", description = "Supprime un maître d'apprentissage du système")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Mentor supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Mentor non trouvé"),
            @ApiResponse(responseCode = "409", description = "Impossible de supprimer, mentor associé à des étudiants")
    })
    @PostMapping("/mentors/{id}/delete")
    public String deleteMentor(
            @Parameter(description = "ID du mentor à supprimer", required = true)
            @PathVariable Long id, 
            RedirectAttributes redirectAttributes) {
        try {
            mentorService.deleteMentor(id);
            
            redirectAttributes.addFlashAttribute("successMessage", 
                "Le maître d'apprentissage a été supprimé avec succès");
            logger.info("Maître d'apprentissage {} supprimé avec succès", id);
            
        } catch (ResponseStatusException ex) {
            logger.error("Erreur lors de la suppression du maître d'apprentissage {}", id, ex);
            redirectAttributes.addFlashAttribute("errorMessage", ex.getReason());
        }
        
        return "redirect:/management/mentors";
    }
}

