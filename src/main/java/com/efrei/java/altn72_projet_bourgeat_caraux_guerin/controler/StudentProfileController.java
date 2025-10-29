package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.controler;

import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto.CompanyDTO;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto.MentorDTO;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto.SchoolYearUpdateDTO;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto.StudentUpdateDTO;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto.VisitDTO;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.service.CompanyService;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.service.MentorService;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.service.StudentProfileService;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.service.StudentService;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.service.VisitService;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.utils.AcademicYearUtils;
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
 * Contrôleur pour la gestion du profil d'un étudiant
 */
@Controller
@RequestMapping("/student")
@Tag(name = "Profil Étudiant", description = "Gestion du profil des étudiants, de leurs années scolaires et visites")
public class StudentProfileController {

    private static final Logger logger = LoggerFactory.getLogger(StudentProfileController.class);

    private final StudentProfileService studentProfileService;
    private final StudentService studentService;
    private final CompanyService companyService;
    private final MentorService mentorService;
    private final VisitService visitService;

    public StudentProfileController(StudentProfileService studentProfileService,
                                   StudentService studentService,
                                   CompanyService companyService,
                                   MentorService mentorService,
                                   VisitService visitService) {
        this.studentProfileService = studentProfileService;
        this.studentService = studentService;
        this.companyService = companyService;
        this.mentorService = mentorService;
        this.visitService = visitService;
    }

    /**
     * Affiche la page de profil d'un étudiant
     * @param studentId L'ID de l'étudiant
     * @param model Le modèle
     * @return Le nom de la vue
     */
    @Operation(summary = "Afficher le profil d'un étudiant", description = "Affiche la page de profil détaillée d'un étudiant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profil affiché avec succès"),
            @ApiResponse(responseCode = "404", description = "Étudiant non trouvé")
    })
    @GetMapping("/{studentId}")
    public String getStudentProfile(
            @Parameter(description = "ID de l'étudiant", required = true)
            @PathVariable Long studentId, 
            Model model) {
        try {
            studentProfileService.prepareProfileModel(model, studentId, null);
            logger.info("Page de profil affichée pour l'étudiant : {}", studentId);
            return "student-profile";
        } catch (ResponseStatusException ex) {
            logger.error("Erreur lors de l'affichage du profil de l'étudiant {}", studentId, ex);
            return "redirect:/";
        }
    }

    /**
     * Met à jour les informations de base d'un étudiant
     * @param studentId L'ID de l'étudiant
     * @param dto Le DTO de mise à jour
     * @param result Les résultats de la validation
     * @param redirectAttributes Les attributs de redirection
     * @return La redirection
     */
    @Operation(summary = "Mettre à jour les informations de base d'un étudiant", description = "Met à jour le nom, prénom, email et téléphone d'un étudiant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Informations mises à jour avec succès"),
            @ApiResponse(responseCode = "400", description = "Erreur de validation des données"),
            @ApiResponse(responseCode = "404", description = "Étudiant non trouvé")
    })
    @PostMapping("/{studentId}/update-basic-info")
    public String updateStudentBasicInfo(
            @Parameter(description = "ID de l'étudiant", required = true)
            @PathVariable Long studentId,
            @Parameter(description = "Nouvelles informations de l'étudiant", required = true)
            @Valid @ModelAttribute StudentUpdateDTO dto,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            logger.error("Erreurs de validation détectées : {}", result.getAllErrors());
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Erreur de validation : veuillez vérifier les champs obligatoires");
            return "redirect:/student/" + studentId;
        }

        try {
            studentService.updateStudentBasicInfo(studentId, dto);
            
            redirectAttributes.addFlashAttribute("successMessage", 
                "Les informations de l'étudiant ont été mises à jour avec succès");
            logger.info("Informations de base mises à jour pour l'étudiant {}", studentId);
            
        } catch (ResponseStatusException ex) {
            logger.error("Erreur lors de la mise à jour des informations de l'étudiant {}", studentId, ex);
            redirectAttributes.addFlashAttribute("errorMessage", ex.getReason());
        }
        
        return "redirect:/student/" + studentId;
    }

    /**
     * Affiche la page de profil d'un étudiant pour une année académique spécifique
     * @param studentId L'ID de l'étudiant
     * @param academicYear L'année académique
     * @param model Le modèle
     * @return Le nom de la vue
     */
    @Operation(summary = "Afficher le profil pour une année", description = "Affiche le profil d'un étudiant pour une année académique spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profil affiché avec succès pour l'année spécifiée"),
            @ApiResponse(responseCode = "404", description = "Étudiant ou année académique non trouvé(e)")
    })
    @GetMapping("/{studentId}/{academicYear}")
    public String getStudentProfileForYear(
            @Parameter(description = "ID de l'étudiant", required = true)
            @PathVariable Long studentId, 
            @Parameter(description = "Année académique au format YYYY-YYYY", required = true)
            @PathVariable String academicYear, 
            Model model) {
        try {
            String formattedAcademicYear = AcademicYearUtils.fromUrlFormat(academicYear);
            studentProfileService.prepareProfileModel(model, studentId, formattedAcademicYear);
            logger.info("Page de profil affichée pour l'étudiant {} - année {}", studentId, formattedAcademicYear);
            return "student-profile";
        } catch (ResponseStatusException ex) {
            logger.error("Erreur lors de l'affichage du profil de l'étudiant {} pour l'année {}", 
                studentId, academicYear, ex);
            return "redirect:/student/" + studentId;
        }
    }

    /**
     * Met à jour les informations d'une année scolaire
     * @param studentId L'ID de l'étudiant
     * @param schoolYearId L'ID de l'année scolaire
     * @param dto Le DTO de mise à jour de l'année scolaire
     * @param result Les résultats de la validation
     * @param redirectAttributes Les attributs de redirection
     * @return La redirection
     */
    @Operation(summary = "Mettre à jour une année scolaire", description = "Met à jour les informations d'une année scolaire d'un étudiant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Année scolaire mise à jour avec succès"),
            @ApiResponse(responseCode = "400", description = "Erreur de validation des données"),
            @ApiResponse(responseCode = "404", description = "Étudiant ou année scolaire non trouvé(e)")
    })
    @PostMapping("/{studentId}/school-year/{schoolYearId}/update")
    public String updateSchoolYear(
            @Parameter(description = "ID de l'étudiant", required = true)
            @PathVariable Long studentId,
            @Parameter(description = "ID de l'année scolaire", required = true)
            @PathVariable Long schoolYearId,
            @Parameter(description = "Nouvelles informations de l'année scolaire", required = true)
            @Valid @ModelAttribute SchoolYearUpdateDTO dto,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            logger.error("Erreurs de validation détectées : {}", result.getAllErrors());
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Erreur de validation : veuillez vérifier les champs obligatoires");
            return "redirect:/student/" + studentId;
        }

        try {
            studentProfileService.updateSchoolYear(schoolYearId, dto);
            
            redirectAttributes.addFlashAttribute("successMessage", 
                "Les informations ont été mises à jour avec succès");
            logger.info("Année scolaire {} mise à jour avec succès", schoolYearId);
            
        } catch (ResponseStatusException ex) {
            logger.error("Erreur lors de la mise à jour de l'année scolaire {}", schoolYearId, ex);
            redirectAttributes.addFlashAttribute("errorMessage", ex.getReason());
        }
        
        return "redirect:/student/" + studentId;
    }

    /**
     * Crée une nouvelle entreprise
     * @param studentId L'ID de l'étudiant
     * @param companyDTO Le DTO de l'entreprise
     * @param result Les résultats de la validation
     * @param redirectAttributes Les attributs de redirection
     * @return La redirection
     */
    @Operation(summary = "Créer une entreprise", description = "Crée une nouvelle entreprise depuis le profil d'un étudiant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Entreprise créée avec succès"),
            @ApiResponse(responseCode = "400", description = "Erreur de validation des données")
    })
    @PostMapping("/{studentId}/company/create")
    public String createCompany(
            @Parameter(description = "ID de l'étudiant", required = true)
            @PathVariable Long studentId,
            @Parameter(description = "Informations de l'entreprise à créer", required = true)
            @Valid @ModelAttribute CompanyDTO companyDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            logger.error("Erreurs de validation détectées : {}", result.getAllErrors());
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Erreur de validation : veuillez vérifier les champs obligatoires");
            return "redirect:/student/" + studentId;
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
        
        return "redirect:/student/" + studentId;
    }

    /**
     * Crée un nouveau mentor
     * @param studentId L'ID de l'étudiant
     * @param mentorDTO Le DTO du mentor
     * @param result Les résultats de la validation
     * @param redirectAttributes Les attributs de redirection
     * @return La redirection
     */
    @Operation(summary = "Créer un mentor", description = "Crée un nouveau maître d'apprentissage depuis le profil d'un étudiant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Mentor créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Erreur de validation des données")
    })
    @PostMapping("/{studentId}/mentor/create")
    public String createMentor(
            @Parameter(description = "ID de l'étudiant", required = true)
            @PathVariable Long studentId,
            @Parameter(description = "Informations du mentor à créer", required = true)
            @Valid @ModelAttribute MentorDTO mentorDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            logger.error("Erreurs de validation détectées : {}", result.getAllErrors());
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Erreur de validation : veuillez vérifier les champs obligatoires");
            return "redirect:/student/" + studentId;
        }

        try {
            mentorService.createMentor(mentorDTO);
            
            redirectAttributes.addFlashAttribute("successMessage", 
                "Le mentor a été créé avec succès");
            logger.info("Mentor {} {} créé avec succès", mentorDTO.getFirstName(), mentorDTO.getLastName());
            
        } catch (ResponseStatusException ex) {
            logger.error("Erreur lors de la création du mentor {} {}", 
                mentorDTO.getFirstName(), mentorDTO.getLastName(), ex);
            redirectAttributes.addFlashAttribute("errorMessage", ex.getReason());
        }
        
        return "redirect:/student/" + studentId;
    }

    /**
     * Crée une nouvelle visite
     * @param studentId L'ID de l'étudiant
     * @param visitDTO Le DTO de la visite
     * @param result Les résultats de la validation
     * @param redirectAttributes Les attributs de redirection
     * @return La redirection
     */
    @Operation(summary = "Créer une visite", description = "Crée une nouvelle visite pour un étudiant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Visite créée avec succès"),
            @ApiResponse(responseCode = "400", description = "Erreur de validation des données")
    })
    @PostMapping("/{studentId}/visit/create")
    public String createVisit(
            @Parameter(description = "ID de l'étudiant", required = true)
            @PathVariable Long studentId,
            @Parameter(description = "Informations de la visite à créer", required = true)
            @Valid @ModelAttribute VisitDTO visitDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            logger.error("Erreurs de validation détectées : {}", result.getAllErrors());
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Erreur de validation : veuillez vérifier les champs obligatoires");
            return "redirect:/student/" + studentId;
        }

        try {
            visitService.createVisit(visitDTO);
            
            redirectAttributes.addFlashAttribute("successMessage", 
                "La visite a été créée avec succès");
            logger.info("Visite créée avec succès pour l'étudiant {}", studentId);
            
        } catch (ResponseStatusException ex) {
            logger.error("Erreur lors de la création de la visite pour l'étudiant {}", studentId, ex);
            redirectAttributes.addFlashAttribute("errorMessage", ex.getReason());
        }
        
        return "redirect:/student/" + studentId;
    }

    /**
     * Met à jour une visite
     * @param studentId L'ID de l'étudiant
     * @param visitId L'ID de la visite
     * @param visitDTO Le DTO de la visite
     * @param result Les résultats de la validation
     * @param redirectAttributes Les attributs de redirection
     * @return La redirection
     */
    @Operation(summary = "Mettre à jour une visite", description = "Met à jour les informations d'une visite existante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Visite mise à jour avec succès"),
            @ApiResponse(responseCode = "400", description = "Erreur de validation des données"),
            @ApiResponse(responseCode = "404", description = "Visite non trouvée")
    })
    @PostMapping("/{studentId}/visit/{visitId}/update")
    public String updateVisit(
            @Parameter(description = "ID de l'étudiant", required = true)
            @PathVariable Long studentId,
            @Parameter(description = "ID de la visite à mettre à jour", required = true)
            @PathVariable Long visitId,
            @Parameter(description = "Nouvelles informations de la visite", required = true)
            @Valid @ModelAttribute VisitDTO visitDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            logger.error("Erreurs de validation détectées : {}", result.getAllErrors());
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Erreur de validation : veuillez vérifier les champs obligatoires");
            return "redirect:/student/" + studentId;
        }

        try {
            visitService.updateVisit(visitId, visitDTO);
            
            redirectAttributes.addFlashAttribute("successMessage", 
                "La visite a été mise à jour avec succès");
            logger.info("Visite {} mise à jour avec succès", visitId);
            
        } catch (ResponseStatusException ex) {
            logger.error("Erreur lors de la mise à jour de la visite {}", visitId, ex);
            redirectAttributes.addFlashAttribute("errorMessage", ex.getReason());
        }
        
        return "redirect:/student/" + studentId;
    }

    /**
     * Supprime une visite
     * @param studentId L'ID de l'étudiant
     * @param visitId L'ID de la visite
     * @param redirectAttributes Les attributs de redirection
     * @return La redirection
     */
    @Operation(summary = "Supprimer une visite", description = "Supprime une visite d'un étudiant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Visite supprimée avec succès"),
            @ApiResponse(responseCode = "404", description = "Visite non trouvée")
    })
    @PostMapping("/{studentId}/visit/{visitId}/delete")
    public String deleteVisit(
            @Parameter(description = "ID de l'étudiant", required = true)
            @PathVariable Long studentId,
            @Parameter(description = "ID de la visite à supprimer", required = true)
            @PathVariable Long visitId,
            RedirectAttributes redirectAttributes) {
        try {
            visitService.deleteVisit(visitId);
            
            redirectAttributes.addFlashAttribute("successMessage", 
                "La visite a été supprimée avec succès");
            logger.info("Visite {} supprimée avec succès", visitId);
            
        } catch (ResponseStatusException ex) {
            logger.error("Erreur lors de la suppression de la visite {}", visitId, ex);
            redirectAttributes.addFlashAttribute("errorMessage", ex.getReason());
        }
        
        return "redirect:/student/" + studentId;
    }
}

