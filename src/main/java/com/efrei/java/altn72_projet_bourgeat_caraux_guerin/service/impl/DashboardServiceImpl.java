package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.service.impl;

import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto.SchoolYearDTO;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto.StudentDTO;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.enums.Program;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.service.StudentService;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.service.SchoolYearService;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.service.DashboardService;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.utils.AcademicYearUtils;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final StudentService studentService;
    private final SchoolYearService schoolYearService;

    public DashboardServiceImpl(StudentService studentService, SchoolYearService schoolYearService) {
        this.studentService = studentService;
        this.schoolYearService = schoolYearService;
    }

    @Override
    public void prepareDashboardModel(Model model, String academicYear, boolean showModal) {
        List<String> academicYears = schoolYearService.getAllAcademicYears();
        String currentYear = academicYear != null ? academicYear : 
            AcademicYearUtils.findClosestAcademicYear(academicYears);

        StudentDTO newStudent = new StudentDTO();
        SchoolYearDTO schoolYearDTO = new SchoolYearDTO();
        schoolYearDTO.setAcademicYear(currentYear);
        List<SchoolYearDTO> schoolYears = new ArrayList<>();
        schoolYears.add(schoolYearDTO);
        newStudent.setSchoolYears(schoolYears);

        model.addAttribute("newStudent", newStudent);
        model.addAttribute("programs", Program.values());
        model.addAttribute("showModal", showModal);
        model.addAttribute("academicYears", academicYears);
        model.addAttribute("currentYear", currentYear);
        model.addAttribute("isDashboard", true);
        model.addAttribute("students", academicYears.isEmpty() ? 
            List.of() : 
            studentService.getStudentsForCurrentYear(currentYear));
    }

    @Override
    public void prepareErrorModel(Model model, StudentDTO studentDTO, BindingResult result, String errorMessage) {
        List<String> academicYears = schoolYearService.getAllAcademicYears();
        String currentYear = (studentDTO.getSchoolYears() != null && !studentDTO.getSchoolYears().isEmpty() 
            && studentDTO.getSchoolYears().get(0) != null && studentDTO.getSchoolYears().get(0).getAcademicYear() != null)
            ? studentDTO.getSchoolYears().get(0).getAcademicYear()
            : AcademicYearUtils.findClosestAcademicYear(academicYears);

        model.addAttribute("students", studentService.getStudentsForCurrentYear(currentYear));
        model.addAttribute("newStudent", studentDTO);
        model.addAttribute("programs", Program.values());
        model.addAttribute("academicYears", academicYears);
        model.addAttribute("currentYear", currentYear);
        model.addAttribute("isDashboard", true);
        model.addAttribute("showModal", true);

        if (result != null) {
            model.addAttribute("errors", result);
        }
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
        }
    }
}
