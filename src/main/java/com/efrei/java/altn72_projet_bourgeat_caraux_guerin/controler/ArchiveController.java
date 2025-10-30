package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.controler;

import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto.StudentDTO;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Contrôleur pour la gestion des archives
 */
@Controller
@RequestMapping("/archives")
@Tag(name = "Archives", description = "Gestion des étudiants archivés")
public class ArchiveController {

    private static final Logger logger = LoggerFactory.getLogger(ArchiveController.class);

    private final StudentService studentService;

    public ArchiveController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * Affiche la page des archives
     * @param model Le modèle
     * @return Le nom de la vue
     */
    @Operation(summary = "Afficher les archives", description = "Affiche la page de tous les étudiants archivés")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Page des archives affichée avec succès"),
            @ApiResponse(responseCode = "500", description = "Erreur lors du chargement des archives")
    })
    @GetMapping
    public String getArchives(Model model) {
        List<StudentDTO> archivedStudents = studentService.getAllArchivedStudents();
        model.addAttribute("archivedStudents", archivedStudents);
        logger.info("Page des archives affichée avec {} étudiant(s) archivé(s)", archivedStudents.size());
        return "archives";
    }

    /**
     * Supprime définitivement un étudiant archivé
     * @param id L'ID de l'étudiant
     * @param redirectAttributes Les attributs de redirection
     * @return Redirection vers la page des archives
     */
    @Operation(summary = "Supprimer un étudiant archivé", description = "Supprime définitivement un étudiant archivé du système")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Étudiant supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Étudiant non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur lors de la suppression")
    })
    @PostMapping("/delete/{id}")
    public String deleteStudent(
            @Parameter(description = "ID de l'étudiant à supprimer définitivement", required = true)
            @PathVariable Long id, 
            RedirectAttributes redirectAttributes) {
        studentService.deleteStudent(id);
        
        logger.info("Étudiant supprimé avec succès");
        redirectAttributes.addFlashAttribute("successMessage",
            "L'étudiant a été supprimé définitivement");
        
        return "redirect:/archives";
    }

    /**
     * Désarchive un étudiant
     * @param id L'ID de l'étudiant
     * @param redirectAttributes Les attributs de redirection
     * @return Redirection vers la page des archives
     */
    @Operation(summary = "Désarchiver un étudiant", description = "Réactive un étudiant archivé et le rend à nouveau actif")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Étudiant désarchivé avec succès"),
            @ApiResponse(responseCode = "404", description = "Étudiant non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur lors du désarchivage")
    })
    @PostMapping("/unarchive/{id}")
    public String unarchiveStudent(
            @Parameter(description = "ID de l'étudiant à désarchiver", required = true)
            @PathVariable Long id, 
            RedirectAttributes redirectAttributes) {
        studentService.unarchiveStudent(id);
        
        logger.info("Étudiant désarchivé avec succès");
        redirectAttributes.addFlashAttribute("successMessage",
            "L'étudiant a été désarchivé avec succès");
        
        return "redirect:/archives";
    }
}

