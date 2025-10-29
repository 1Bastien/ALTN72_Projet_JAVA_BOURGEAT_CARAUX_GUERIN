package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.controler;

import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto.SearchCriteriaDTO;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto.StudentDTO;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.service.DashboardService;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.service.StudentService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.utils.AcademicYearUtils;

/**
 * Contrôleur pour la gestion des étudiants
 */
@Controller
@RequestMapping("/")
@Tag(name = "Étudiants", description = "Gestion des étudiants et du tableau de bord")
public class StudentController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    private final StudentService studentService;
    private final DashboardService dashboardService;

    public StudentController(StudentService studentService, DashboardService dashboardService) {
        this.studentService = studentService;
        this.dashboardService = dashboardService;
    }

    /**
     * Affiche le dashboard
     * @param model Le modèle
     * @return Le nom de la vue
     */
    @Operation(summary = "Afficher le tableau de bord", description = "Affiche le tableau de bord avec l'année académique la plus proche")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dashboard affiché avec succès")
    })
    @GetMapping
    public String getDashboard(Model model) {
        try {
            dashboardService.prepareDashboardModel(model, null, false);
            logger.info("Dashboard affiché");
            return "dashboard";
        } catch (ResponseStatusException e) {
            logger.error("Erreur lors de l'affichage du dashboard", e);
            model.addAttribute("errorMessage", e.getReason());
            dashboardService.prepareDashboardModel(model, null, false);
            return "dashboard";
        }
    }

    /**
     * Affiche le dashboard pour une année académique spécifique
     * @param academicYear L'année académique
     * @param model Le modèle
     * @return Le nom de la vue
     */
    @Operation(summary = "Afficher le tableau de bord par année", description = "Affiche le tableau de bord pour une année académique spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dashboard affiché avec succès pour l'année spécifiée")
    })
    @GetMapping("/{academicYear:\\d{4}-\\d{4}}")
    public String getStudentsByAcademicYear(
            @Parameter(description = "Année académique au format YYYY-YYYY (ex: 2024-2025)", required = true)
            @PathVariable("academicYear") String academicYear, 
            Model model) {
        try {
            String formattedAcademicYear = AcademicYearUtils.fromUrlFormat(academicYear);
            dashboardService.prepareDashboardModel(model, formattedAcademicYear, false);
            logger.info("Dashboard affiché pour l'année académique : {}", formattedAcademicYear);
            return "dashboard";
        } catch (ResponseStatusException e) {
            logger.error("Erreur lors de l'affichage du dashboard pour l'année {}", academicYear, e);
            model.addAttribute("errorMessage", e.getReason());
            dashboardService.prepareDashboardModel(model, null, false);
            return "dashboard";
        }
    }

    /**
     * Crée un étudiant
     * @param studentDTO Le DTO de l'étudiant
     * @param result Les résultats de la validation
     * @param model Le modèle
     * @param redirectAttributes Les attributs de redirection
     * @return Le nom de la vue
     */
    @Operation(summary = "Créer un étudiant", description = "Crée un nouvel étudiant dans le système")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Étudiant créé avec succès, redirection vers le dashboard"),
            @ApiResponse(responseCode = "400", description = "Erreur de validation des données")
    })
    @PostMapping("/")
    public String createStudent(
            @Parameter(description = "Informations de l'étudiant à créer", required = true)
            @Valid StudentDTO studentDTO, 
            BindingResult result, 
            Model model, 
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            logger.error("Erreurs de validation détectées : {}", result.getAllErrors());
            dashboardService.prepareErrorModel(model, studentDTO, result, null);
            return "dashboard";
        }

        try {
            studentService.saveStudent(studentDTO);
            logger.info("Étudiant créé avec succès : {} {}", studentDTO.getFirstName(), studentDTO.getLastName());
            redirectAttributes.addFlashAttribute("successMessage",
                String.format("L'étudiant %s %s a été créé avec succès",
                    studentDTO.getFirstName(), studentDTO.getLastName()));
            return "redirect:/";
        } catch (ResponseStatusException e) {
            logger.error("Erreur lors de la création de l'étudiant {} {}", studentDTO.getFirstName(), studentDTO.getLastName(), e);
            dashboardService.prepareErrorModel(model, studentDTO, null, e.getMessage());
            return "dashboard";
        }
    }

    /**
     * Crée la prochaine année académique pour tous les étudiants
     * @param academicYear L'année académique actuelle
     * @param redirectAttributes Les attributs de redirection
     * @return Redirection vers le dashboard
     */
    @Operation(summary = "Passer à l'année suivante", description = "Crée la prochaine année académique pour tous les étudiants actifs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Passage à l'année suivante effectué avec succès"),
            @ApiResponse(responseCode = "500", description = "Erreur lors du passage à l'année suivante")
    })
    @PostMapping("/progress-year")
    public String progressToNextYear(
            @Parameter(description = "Année académique actuelle", required = true)
            @RequestParam("academicYear") String academicYear, 
            RedirectAttributes redirectAttributes) {
        try {
            String formattedAcademicYear = AcademicYearUtils.fromUrlFormat(academicYear);
            String resultMessage = studentService.progressToNextAcademicYear(formattedAcademicYear);
            
            logger.info("Passage à l'année suivante effectué : {}", resultMessage);
            redirectAttributes.addFlashAttribute("successMessage", resultMessage);
            
            String nextAcademicYear = AcademicYearUtils.getNextAcademicYear(formattedAcademicYear);
            String nextAcademicYearUrl = AcademicYearUtils.toUrlFormat(nextAcademicYear);
            
            return "redirect:/" + nextAcademicYearUrl;
        } catch (ResponseStatusException e) {
            logger.error("Erreur lors du passage à l'année suivante", e);
            redirectAttributes.addFlashAttribute("errorMessage", e.getReason());
            return "redirect:/";
        }
    }

    /**
     * Recherche des étudiants selon plusieurs critères
     * @param criteria Les critères de recherche
     * @return La liste des étudiants correspondant aux critères
     */
    @Operation(summary = "Rechercher des étudiants", description = "Recherche des étudiants selon plusieurs critères (nom, entreprise, mission, année académique)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recherche effectuée avec succès"),
            @ApiResponse(responseCode = "500", description = "Erreur lors de la recherche")
    })
    @PostMapping("/search")
    public ResponseEntity<List<StudentDTO>> searchStudents(
            @Parameter(description = "Critères de recherche", required = true)
            @RequestBody SearchCriteriaDTO criteria) {
        logger.info("Recherche en cours avec les critères : {}", criteria);
        List<StudentDTO> students = studentService.searchStudents(criteria);
        logger.info("Recherche terminée : {} étudiant(s) trouvé(s)", students.size());
        return ResponseEntity.ok(students);
    }
}