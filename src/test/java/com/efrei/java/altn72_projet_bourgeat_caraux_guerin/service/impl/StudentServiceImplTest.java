package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.service.impl;

import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto.SchoolYearDTO;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto.SearchCriteriaDTO;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto.StudentDTO;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.SchoolYear;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.Student;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.repository.SchoolYearRepository;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.repository.StudentRepository;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.enums.Program;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.mapper.SchoolYearMapper;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.mapper.StudentMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour StudentServiceImpl
 */
@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private SchoolYearRepository schoolYearRepository;

    @Mock
    private StudentMapper studentMapper;

    @Mock
    private SchoolYearMapper schoolYearMapper;

    @InjectMocks
    private StudentServiceImpl studentService;

    @Test
    void testGetStudentsForCurrentYear() {
        // Arrange
        String academicYear = "2024/2025";
        Student student1 = new Student();
        student1.setId(1L);
        student1.setLastName("Dupont");

        Student student2 = new Student();
        student2.setId(2L);
        student2.setLastName("Martin");

        List<Student> students = Arrays.asList(student1, student2);

        StudentDTO dto1 = new StudentDTO();
        dto1.setId(1L);
        dto1.setLastName("Dupont");

        StudentDTO dto2 = new StudentDTO();
        dto2.setId(2L);
        dto2.setLastName("Martin");

        List<StudentDTO> expectedDTOs = Arrays.asList(dto1, dto2);

        when(studentRepository.findBySchoolYears_AcademicYear(academicYear)).thenReturn(students);
        when(studentMapper.toDTOList(students)).thenReturn(expectedDTOs);

        // Act
        List<StudentDTO> result = studentService.getStudentsForCurrentYear(academicYear);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(studentRepository, times(1)).findBySchoolYears_AcademicYear(academicYear);
        verify(studentMapper, times(1)).toDTOList(students);
    }

    @Test
    void testGetStudentById() {
        // Arrange
        Long studentId = 1L;
        Student student = new Student();
        student.setId(studentId);
        student.setLastName("Dupont");
        student.setFirstName("Jean");

        StudentDTO expectedDTO = new StudentDTO();
        expectedDTO.setId(studentId);
        expectedDTO.setLastName("Dupont");
        expectedDTO.setFirstName("Jean");

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(studentMapper.toDTO(student)).thenReturn(expectedDTO);

        // Act
        StudentDTO result = studentService.getStudentById(studentId);

        // Assert
        assertNotNull(result);
        assertEquals(studentId, result.getId());
        assertEquals("Dupont", result.getLastName());
        verify(studentRepository, times(1)).findById(studentId);
        verify(studentMapper, times(1)).toDTO(student);
    }

    @Test
    void testSaveStudent() {
        // Arrange
        StudentDTO inputDTO = new StudentDTO();
        inputDTO.setLastName("Nouveau");
        inputDTO.setFirstName("Etudiant");
        inputDTO.setEmail("nouveau@example.com");

        SchoolYearDTO schoolYearDTO = new SchoolYearDTO();
        schoolYearDTO.setAcademicYear("2024/2025");
        schoolYearDTO.setProgram(Program.L1_APP);
        inputDTO.setSchoolYears(Arrays.asList(schoolYearDTO));

        Student student = new Student();
        student.setLastName("Nouveau");
        student.setFirstName("Etudiant");
        student.setEmail("nouveau@example.com");

        SchoolYear schoolYear = new SchoolYear();
        schoolYear.setAcademicYear("2024/2025");
        schoolYear.setProgram(Program.L1_APP);

        Student savedStudent = new Student();
        savedStudent.setId(1L);
        savedStudent.setLastName("Nouveau");
        savedStudent.setFirstName("Etudiant");
        savedStudent.setEmail("nouveau@example.com");

        StudentDTO expectedDTO = new StudentDTO();
        expectedDTO.setId(1L);
        expectedDTO.setLastName("Nouveau");

        when(studentMapper.toEntity(inputDTO)).thenReturn(student);
        when(schoolYearMapper.toEntity(schoolYearDTO)).thenReturn(schoolYear);
        when(studentRepository.save(any(Student.class))).thenReturn(savedStudent);
        when(studentMapper.toDTO(savedStudent)).thenReturn(expectedDTO);

        // Act
        StudentDTO result = studentService.saveStudent(inputDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(studentMapper, times(1)).toEntity(inputDTO);
        verify(studentRepository, times(1)).save(any(Student.class));
        verify(studentMapper, times(1)).toDTO(savedStudent);
    }

    @Test
    void testProgressToNextAcademicYear() {
        // Arrange
        String currentAcademicYear = "2024/2025";
        String nextAcademicYear = "2025/2026";

        Student student = new Student();
        student.setId(1L);
        student.setLastName("Dupont");
        student.setFirstName("Jean");

        SchoolYear currentSchoolYear = new SchoolYear();
        currentSchoolYear.setAcademicYear(currentAcademicYear);
        currentSchoolYear.setProgram(Program.L1_APP);
        currentSchoolYear.setStudent(student);

        student.setSchoolYears(Arrays.asList(currentSchoolYear));

        List<Student> students = Arrays.asList(student);

        when(studentRepository.findBySchoolYears_AcademicYear(currentAcademicYear)).thenReturn(students);
        when(schoolYearRepository.findByStudentIdAndAcademicYear(student.getId(), nextAcademicYear))
            .thenReturn(Optional.empty());
        when(schoolYearRepository.save(any(SchoolYear.class))).thenReturn(new SchoolYear());

        // Act
        String result = studentService.progressToNextAcademicYear(currentAcademicYear);

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("2025/2026"));
        assertTrue(result.contains("1 année(s) scolaire(s) créée(s)"));
        verify(studentRepository, times(1)).findBySchoolYears_AcademicYear(currentAcademicYear);
        verify(schoolYearRepository, times(1)).save(any(SchoolYear.class));
    }

    @Test
    void testGetAllArchivedStudents() {
        // Arrange
        Student student1 = new Student();
        student1.setId(1L);
        student1.setLastName("Archivé1");
        student1.setIsArchived(true);

        Student student2 = new Student();
        student2.setId(2L);
        student2.setLastName("Archivé2");
        student2.setIsArchived(true);

        List<Student> archivedStudents = Arrays.asList(student1, student2);

        StudentDTO dto1 = new StudentDTO();
        dto1.setId(1L);
        dto1.setLastName("Archivé1");

        StudentDTO dto2 = new StudentDTO();
        dto2.setId(2L);
        dto2.setLastName("Archivé2");

        List<StudentDTO> expectedDTOs = Arrays.asList(dto1, dto2);

        when(studentRepository.findAllArchivedStudents()).thenReturn(archivedStudents);
        when(studentMapper.toDTOList(archivedStudents)).thenReturn(expectedDTOs);

        // Act
        List<StudentDTO> result = studentService.getAllArchivedStudents();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(studentRepository, times(1)).findAllArchivedStudents();
        verify(studentMapper, times(1)).toDTOList(archivedStudents);
    }

    @Test
    void testDeleteStudent() {
        // Arrange
        Long studentId = 1L;
        Student student = new Student();
        student.setId(studentId);
        student.setLastName("Dupont");
        student.setFirstName("Jean");

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        doNothing().when(studentRepository).delete(student);

        // Act
        studentService.deleteStudent(studentId);

        // Assert
        verify(studentRepository, times(1)).findById(studentId);
        verify(studentRepository, times(1)).delete(student);
    }

    @Test
    void testSearchStudents_AllCriteria() {
        // Arrange
        SearchCriteriaDTO criteria = new SearchCriteriaDTO();
        criteria.setName("Dupont");
        criteria.setCompany("Google");
        criteria.setMissionKeyword("Java");
        criteria.setAcademicYear("2024/2025");

        Student student1 = new Student();
        student1.setId(1L);
        student1.setLastName("Dupont");
        student1.setFirstName("Jean");

        Student student2 = new Student();
        student2.setId(2L);
        student2.setLastName("Dupont");
        student2.setFirstName("Marie");

        List<Student> students = Arrays.asList(student1, student2);

        StudentDTO dto1 = new StudentDTO();
        dto1.setId(1L);
        dto1.setLastName("Dupont");
        dto1.setFirstName("Jean");

        StudentDTO dto2 = new StudentDTO();
        dto2.setId(2L);
        dto2.setLastName("Dupont");
        dto2.setFirstName("Marie");

        List<StudentDTO> expectedDTOs = Arrays.asList(dto1, dto2);

        when(studentRepository.searchStudents("Dupont", "Google", "Java", "2024/2025")).thenReturn(students);
        when(studentMapper.toDTOList(students)).thenReturn(expectedDTOs);

        // Act
        List<StudentDTO> result = studentService.searchStudents(criteria);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(studentRepository, times(1)).searchStudents("Dupont", "Google", "Java", "2024/2025");
        verify(studentMapper, times(1)).toDTOList(students);
    }
}

