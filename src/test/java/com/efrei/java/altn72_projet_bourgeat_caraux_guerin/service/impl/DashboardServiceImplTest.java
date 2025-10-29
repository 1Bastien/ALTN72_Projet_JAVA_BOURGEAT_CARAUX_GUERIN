package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.service.impl;

import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto.SchoolYearDTO;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto.StudentDTO;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.enums.Program;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.service.SchoolYearService;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.service.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour DashboardServiceImpl
 */
@ExtendWith(MockitoExtension.class)
class DashboardServiceImplTest {

    @Mock
    private StudentService studentService;

    @Mock
    private SchoolYearService schoolYearService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private DashboardServiceImpl dashboardService;

    @Test
    void testPrepareDashboardModel_WithAcademicYear() {
        // Arrange
        String academicYear = "2024/2025";
        boolean showModal = false;
        List<String> academicYears = Arrays.asList("2023/2024", "2024/2025", "2025/2026");
        List<StudentDTO> students = Arrays.asList(new StudentDTO(), new StudentDTO());

        when(schoolYearService.getAllAcademicYears()).thenReturn(academicYears);
        when(studentService.getStudentsForCurrentYear(academicYear)).thenReturn(students);
        when(model.addAttribute(anyString(), any())).thenReturn(model);

        // Act
        dashboardService.prepareDashboardModel(model, academicYear, showModal);

        // Assert
        verify(schoolYearService, times(1)).getAllAcademicYears();
        verify(studentService, times(1)).getStudentsForCurrentYear(academicYear);
        verify(model, times(1)).addAttribute(eq("newStudent"), any(StudentDTO.class));
        verify(model, times(1)).addAttribute("programs", Program.values());
        verify(model, times(1)).addAttribute("showModal", showModal);
        verify(model, times(1)).addAttribute("academicYears", academicYears);
        verify(model, times(1)).addAttribute("currentYear", academicYear);
        verify(model, times(1)).addAttribute("isDashboard", true);
        verify(model, times(1)).addAttribute("students", students);
    }

    @Test
    void testPrepareErrorModel_WithBindingResult() {
        // Arrange
        StudentDTO studentDTO = new StudentDTO();
        SchoolYearDTO schoolYearDTO = new SchoolYearDTO();
        schoolYearDTO.setAcademicYear("2024/2025");
        studentDTO.setSchoolYears(Arrays.asList(schoolYearDTO));

        List<String> academicYears = Arrays.asList("2023/2024", "2024/2025", "2025/2026");
        List<StudentDTO> students = Arrays.asList(new StudentDTO(), new StudentDTO());
        String errorMessage = "Une erreur est survenue";

        when(schoolYearService.getAllAcademicYears()).thenReturn(academicYears);
        when(studentService.getStudentsForCurrentYear("2024/2025")).thenReturn(students);
        when(model.addAttribute(anyString(), any())).thenReturn(model);

        // Act
        dashboardService.prepareErrorModel(model, studentDTO, bindingResult, errorMessage);

        // Assert
        verify(schoolYearService, times(1)).getAllAcademicYears();
        verify(studentService, times(1)).getStudentsForCurrentYear("2024/2025");
        verify(model, times(1)).addAttribute("students", students);
        verify(model, times(1)).addAttribute("newStudent", studentDTO);
        verify(model, times(1)).addAttribute("programs", Program.values());
        verify(model, times(1)).addAttribute("academicYears", academicYears);
        verify(model, times(1)).addAttribute("currentYear", "2024/2025");
        verify(model, times(1)).addAttribute("isDashboard", true);
        verify(model, times(1)).addAttribute("showModal", true);
        verify(model, times(1)).addAttribute("errors", bindingResult);
        verify(model, times(1)).addAttribute("errorMessage", errorMessage);
    }
}

