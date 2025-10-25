package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.service.impl;

import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto.StudentDTO;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.SchoolYear;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.Student;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.repository.StudentRepository;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.mapper.SchoolYearMapper;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.mapper.StudentMapper;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final SchoolYearMapper schoolYearMapper;

    public StudentServiceImpl(StudentRepository studentRepository, 
                            StudentMapper studentMapper,
                            SchoolYearMapper schoolYearMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
        this.schoolYearMapper = schoolYearMapper;
    }

    @Override
    public List<StudentDTO> getStudentsForCurrentYear(String academicYear) {
        try {
            List<Student> students = studentRepository.findBySchoolYears_academicYear(academicYear);
            return students.isEmpty() ? List.of() : studentMapper.toDTOList(students);
        } catch (Exception ex) {
            logger.error("Erreur lors de la récupération des étudiants", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Une erreur est survenue lors de la récupération des étudiants");
        }
    }

    @Override
    public StudentDTO getStudentById(Long id) {
        return studentMapper.toDTO(studentRepository.findById(id)
            .orElseThrow(() -> {
                logger.error("Aucun étudiant trouvé avec l'ID : {}", id);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, 
                    String.format("L'étudiant avec l'identifiant %d est introuvable", id));
            }));
    }

    @Override
    @Transactional
    public StudentDTO saveStudent(StudentDTO studentDTO) {
        try {
            Student student = studentMapper.toEntity(studentDTO);
            
            if (studentDTO.getSchoolYears() != null && !studentDTO.getSchoolYears().isEmpty()) {
                List<SchoolYear> schoolYears = studentDTO.getSchoolYears().stream()
                    .map(schoolYearDTO -> {
                        SchoolYear schoolYear = schoolYearMapper.toEntity(schoolYearDTO);
                        schoolYear.setStudent(student);
                        return schoolYear;
                    })
                    .toList();
                student.setSchoolYears(schoolYears);
            }
            
            Student savedStudent = studentRepository.save(student);
            
            return studentMapper.toDTO(savedStudent);
            
        } catch (DataIntegrityViolationException ex) {
            logger.error("Violation de contrainte d'unicité pour l'email : {}", studentDTO.getEmail());
            throw new ResponseStatusException(HttpStatus.CONFLICT, 
                String.format("L'adresse email '%s' est déjà utilisée", studentDTO.getEmail()));
        } catch (Exception ex) { //changer exception
            logger.error("Erreur lors de la création de l'étudiant {} {}",
                        studentDTO.getFirstName(), studentDTO.getLastName(), ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                "Une erreur est survenue lors de la création de l'étudiant");
        }
    }
}
