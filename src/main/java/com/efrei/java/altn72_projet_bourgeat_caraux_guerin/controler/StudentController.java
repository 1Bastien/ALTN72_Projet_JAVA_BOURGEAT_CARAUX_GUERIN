package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.controler;

import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto.StudentDTO;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.service.DashboardService;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.service.StudentService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Contrôleur pour la gestion des étudiants
 */
@Controller
@RequestMapping("/")
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
    @GetMapping
    public String getDashboard(Model model) {
        dashboardService.prepareDashboardModel(model, null, false);
        logger.info("Dashboard affiché");
        return "dashboard";
    }

    /**
     * Affiche le dashboard pour une année académique spécifique
     * @param academicYear L'année académique
     * @param model Le modèle
     * @return Le nom de la vue
     */
    @GetMapping("/{academicYear}")
    public String getStudentsByAcademicYear(@PathVariable String academicYear, Model model) {
        String formattedAcademicYear = academicYear.replace("-", "/");
        dashboardService.prepareDashboardModel(model, formattedAcademicYear, false);
        logger.info("Dashboard affiché pour l'année académique : {}", formattedAcademicYear);
        return "dashboard";
    }

    /**
     * Crée un étudiant
     * @param studentDTO Le DTO de l'étudiant
     * @param result Les résultats de la validation
     * @param model Le modèle
     * @param redirectAttributes Les attributs de redirection
     * @return Le nom de la vue
     */
    @PostMapping("/")
    public String createStudent(@Valid StudentDTO studentDTO, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
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
}