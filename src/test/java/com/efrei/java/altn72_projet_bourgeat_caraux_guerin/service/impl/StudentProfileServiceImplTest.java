package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.service.impl;

import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto.CompanyDTO;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto.MentorDTO;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto.SchoolYearUpdateDTO;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto.VisitDTO;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.*;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.repository.*;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.enums.Program;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.mapper.SchoolYearMapper;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.service.CompanyService;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.service.MentorService;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.service.VisitService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour StudentProfileServiceImpl
 */
@ExtendWith(MockitoExtension.class)
class StudentProfileServiceImplTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private SchoolYearRepository schoolYearRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private MentorRepository mentorRepository;

    @Mock
    private CompanyService companyService;

    @Mock
    private MentorService mentorService;

    @Mock
    private VisitService visitService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SchoolYearMapper schoolYearMapper;

    @Mock
    private Model model;

    @InjectMocks
    private StudentProfileServiceImpl studentProfileService;

    @Test
    void testPrepareProfileModel() {
        // Arrange
        Long studentId = 1L;
        String academicYear = "2024/2025";

        Student student = new Student();
        student.setId(studentId);
        student.setLastName("Dupont");

        SchoolYear schoolYear1 = new SchoolYear();
        schoolYear1.setId(1L);
        schoolYear1.setAcademicYear("2024/2025");

        SchoolYear schoolYear2 = new SchoolYear();
        schoolYear2.setId(2L);
        schoolYear2.setAcademicYear("2023/2024");

        List<SchoolYear> schoolYears = Arrays.asList(schoolYear1, schoolYear2);
        List<CompanyDTO> companies = Arrays.asList(new CompanyDTO());
        List<MentorDTO> mentors = Arrays.asList(new MentorDTO());
        List<User> users = Arrays.asList(new User());
        List<VisitDTO> visits = Arrays.asList(new VisitDTO());

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(schoolYearRepository.findByStudentId(studentId)).thenReturn(schoolYears);
        when(schoolYearRepository.findByStudentIdAndAcademicYear(studentId, academicYear))
            .thenReturn(Optional.of(schoolYear1));
        when(companyService.getAllCompanies()).thenReturn(companies);
        when(mentorService.getAllMentors()).thenReturn(mentors);
        when(userRepository.findAll()).thenReturn(users);
        when(visitService.getVisitsBySchoolYearId(1L)).thenReturn(visits);
        when(model.addAttribute(anyString(), any())).thenReturn(model);

        // Act
        studentProfileService.prepareProfileModel(model, studentId, academicYear);

        // Assert
        verify(studentRepository, times(1)).findById(studentId);
        verify(schoolYearRepository, times(1)).findByStudentId(studentId);
        verify(schoolYearRepository, times(1)).findByStudentIdAndAcademicYear(studentId, academicYear);
        verify(companyService, times(1)).getAllCompanies();
        verify(mentorService, times(1)).getAllMentors();
        verify(userRepository, times(1)).findAll();
        verify(visitService, times(1)).getVisitsBySchoolYearId(1L);
        verify(model, times(1)).addAttribute("student", student);
        verify(model, times(1)).addAttribute("currentYear", academicYear);
    }

    @Test
    void testUpdateSchoolYear() {
        // Arrange
        Long schoolYearId = 1L;
        Long companyId = 1L;
        Long mentorId = 1L;

        SchoolYearUpdateDTO dto = new SchoolYearUpdateDTO();
        dto.setProgram(Program.L3_APP);
        dto.setMajor("Informatique");
        dto.setCompanyId(companyId);
        dto.setMentorId(mentorId);
        dto.setMissionKeywords("Java, Spring");

        SchoolYear schoolYear = new SchoolYear();
        schoolYear.setId(schoolYearId);
        schoolYear.setAcademicYear("2024/2025");

        Company company = new Company();
        company.setId(companyId);

        Mentor mentor = new Mentor();
        mentor.setId(mentorId);

        SchoolYear updatedSchoolYear = new SchoolYear();
        updatedSchoolYear.setId(schoolYearId);
        updatedSchoolYear.setProgram(Program.L3_APP);

        when(schoolYearRepository.findById(schoolYearId)).thenReturn(Optional.of(schoolYear));
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        when(mentorRepository.findById(mentorId)).thenReturn(Optional.of(mentor));
        doNothing().when(schoolYearMapper).updateFromDTO(dto, schoolYear);
        when(schoolYearRepository.save(any(SchoolYear.class))).thenReturn(updatedSchoolYear);

        // Act
        SchoolYear result = studentProfileService.updateSchoolYear(schoolYearId, dto);

        // Assert
        assertNotNull(result);
        verify(schoolYearRepository, times(1)).findById(schoolYearId);
        verify(companyRepository, times(1)).findById(companyId);
        verify(mentorRepository, times(1)).findById(mentorId);
        verify(schoolYearMapper, times(1)).updateFromDTO(dto, schoolYear);
        verify(schoolYearRepository, times(1)).save(any(SchoolYear.class));
    }

    @Test
    void testGetSchoolYearByStudentIdAndAcademicYear() {
        // Arrange
        Long studentId = 1L;
        String academicYear = "2024/2025";

        SchoolYear schoolYear = new SchoolYear();
        schoolYear.setId(1L);
        schoolYear.setAcademicYear(academicYear);

        when(schoolYearRepository.findByStudentIdAndAcademicYear(studentId, academicYear))
            .thenReturn(Optional.of(schoolYear));

        // Act
        SchoolYear result = studentProfileService.getSchoolYearByStudentIdAndAcademicYear(studentId, academicYear);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(academicYear, result.getAcademicYear());
        verify(schoolYearRepository, times(1)).findByStudentIdAndAcademicYear(studentId, academicYear);
    }

    @Test
    void testGetSchoolYearById() {
        // Arrange
        Long schoolYearId = 1L;

        SchoolYear schoolYear = new SchoolYear();
        schoolYear.setId(schoolYearId);
        schoolYear.setAcademicYear("2024/2025");

        when(schoolYearRepository.findById(schoolYearId)).thenReturn(Optional.of(schoolYear));

        // Act
        SchoolYear result = studentProfileService.getSchoolYearById(schoolYearId);

        // Assert
        assertNotNull(result);
        assertEquals(schoolYearId, result.getId());
        verify(schoolYearRepository, times(1)).findById(schoolYearId);
    }

    @Test
    void testGetCompanyEntityById() {
        // Arrange
        Long companyId = 1L;

        Company company = new Company();
        company.setId(companyId);
        company.setCompanyName("Entreprise Test");

        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));

        // Act
        Company result = studentProfileService.getCompanyEntityById(companyId);

        // Assert
        assertNotNull(result);
        assertEquals(companyId, result.getId());
        assertEquals("Entreprise Test", result.getCompanyName());
        verify(companyRepository, times(1)).findById(companyId);
    }

    @Test
    void testGetMentorEntityById() {
        // Arrange
        Long mentorId = 1L;

        Mentor mentor = new Mentor();
        mentor.setId(mentorId);
        mentor.setLastName("Dupont");

        when(mentorRepository.findById(mentorId)).thenReturn(Optional.of(mentor));

        // Act
        Mentor result = studentProfileService.getMentorEntityById(mentorId);

        // Assert
        assertNotNull(result);
        assertEquals(mentorId, result.getId());
        assertEquals("Dupont", result.getLastName());
        verify(mentorRepository, times(1)).findById(mentorId);
    }
}

