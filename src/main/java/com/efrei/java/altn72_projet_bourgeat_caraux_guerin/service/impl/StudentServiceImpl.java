package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.service.impl;

import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.exception.DatabaseException;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.exception.ResourceNotFoundException;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto.SearchCriteriaDTO;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto.StudentDTO;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto.StudentUpdateDTO;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.SchoolYear;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.Student;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.repository.SchoolYearRepository;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.repository.StudentRepository;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.enums.Program;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.mapper.SchoolYearMapper;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.mapper.StudentMapper;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.service.StudentService;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.utils.AcademicYearUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

    private final StudentRepository studentRepository;
    private final SchoolYearRepository schoolYearRepository;
    private final StudentMapper studentMapper;
    private final SchoolYearMapper schoolYearMapper;

    public StudentServiceImpl(StudentRepository studentRepository,
                            SchoolYearRepository schoolYearRepository,
                            StudentMapper studentMapper,
                            SchoolYearMapper schoolYearMapper) {
        this.studentRepository = studentRepository;
        this.schoolYearRepository = schoolYearRepository;
        this.studentMapper = studentMapper;
        this.schoolYearMapper = schoolYearMapper;
    }

    @Override
    public List<StudentDTO> getStudentsForCurrentYear(String academicYear) {
        try {
            List<Student> students = studentRepository.findBySchoolYears_AcademicYear(academicYear);
            return students.isEmpty() ? List.of() : studentMapper.toDTOList(students);
        } catch (DataAccessException ex) {
            logger.error("Erreur d'accès aux données lors de la récupération des étudiants", ex);
            throw new DatabaseException(
                "Une erreur de base de données est survenue lors de la récupération des étudiants", ex);
        }
    }

    @Override
    public StudentDTO getStudentById(Long id) {
        return studentMapper.toDTO(studentRepository.findById(id)
            .orElseThrow(() -> {
                logger.error("Aucun étudiant trouvé avec l'ID : {}", id);
                return new ResourceNotFoundException(
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
            throw ex;
        } catch (DataAccessException ex) {
            logger.error("Erreur d'accès aux données lors de la création de l'étudiant {} {}",
                        studentDTO.getFirstName(), studentDTO.getLastName(), ex);
            throw new DatabaseException(
                "Une erreur de base de données est survenue lors de la création de l'étudiant", ex);
        }
    }

    @Override
    @Transactional
    public String progressToNextAcademicYear(String currentAcademicYear) {
        try {
            List<Student> students = studentRepository.findBySchoolYears_AcademicYear(currentAcademicYear);
            
            if (students.isEmpty()) {
                return "Aucun étudiant trouvé pour l'année " + currentAcademicYear;
            }

            String nextAcademicYear = AcademicYearUtils.getNextAcademicYear(currentAcademicYear);
            int createdCount = 0;
            int archivedCount = 0;
            int alreadyHasNextYearCount = 0;
            
            for (Student student : students) {
                SchoolYear currentSchoolYear = findCurrentSchoolYear(student, currentAcademicYear);
                
                if (currentSchoolYear == null || currentSchoolYear.getProgram() == null) {
                    continue;
                }
                
                Program currentProgram = currentSchoolYear.getProgram();

                if (currentProgram == Program.M2_APP) {
                    archiveStudent(student);
                    archivedCount++;
                    continue;
                }

                if (hasSchoolYearForNextYear(student.getId(), nextAcademicYear)) {
                    alreadyHasNextYearCount++;
                    logger.info("Étudiant {} {} a déjà une année scolaire pour {}", 
                        student.getFirstName(), student.getLastName(), nextAcademicYear);
                    continue;
                }

                createNewSchoolYear(student, currentProgram, nextAcademicYear);
                createdCount++;
            }
            
            String resultMessage = buildProgressionMessage(nextAcademicYear, createdCount, archivedCount, alreadyHasNextYearCount);
            
            logger.info("Progression vers {} terminée : {} créés, {} archivés, {} déjà existants", 
                nextAcademicYear, createdCount, archivedCount, alreadyHasNextYearCount);
            
            return resultMessage;
            
        } catch (DataAccessException ex) {
            logger.error("Erreur d'accès aux données lors du passage à la prochaine année académique", ex);
            throw new DatabaseException(
                "Une erreur de base de données est survenue lors du passage à la prochaine année académique", ex);
        }
    }
    
    /**
     * Trouve l'année scolaire actuelle d'un étudiant
     * @param student L'étudiant
     * @param academicYear L'année académique
     * @return L'année scolaire ou null si non trouvée
     */
    private SchoolYear findCurrentSchoolYear(Student student, String academicYear) {
        return student.getSchoolYears().stream()
            .filter(sy -> sy.getAcademicYear().equals(academicYear))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Archive un étudiant
     * @param student L'étudiant à archiver
     */
    private void archiveStudent(Student student) {
        student.setIsArchived(true);
        studentRepository.save(student);
        logger.info("Étudiant {} {} archivé (était en M2_APP)", 
            student.getFirstName(), student.getLastName());
    }
    
    /**
     * Vérifie si un étudiant a déjà une année scolaire pour l'année suivante
     * @param studentId L'ID de l'étudiant
     * @param nextAcademicYear L'année académique suivante
     * @return true si l'année existe déjà, false sinon
     */
    private boolean hasSchoolYearForNextYear(Long studentId, String nextAcademicYear) {
        return schoolYearRepository
            .findByStudentIdAndAcademicYear(studentId, nextAcademicYear)
            .isPresent();
    }
    
    /**
     * Crée une nouvelle année scolaire pour un étudiant
     * @param student L'étudiant
     * @param currentProgram Le programme actuel
     * @param nextAcademicYear L'année académique suivante
     */
    private void createNewSchoolYear(Student student, Program currentProgram, String nextAcademicYear) {
        Program nextProgram = getNextProgram(currentProgram);
        
        SchoolYear newSchoolYear = new SchoolYear();
        newSchoolYear.setAcademicYear(nextAcademicYear);
        newSchoolYear.setProgram(nextProgram);
        newSchoolYear.setStudent(student);
        
        schoolYearRepository.save(newSchoolYear);
        logger.info("Nouvelle année scolaire créée pour {} {} : {} - {}", 
            student.getFirstName(), student.getLastName(), nextAcademicYear, nextProgram);
    }
    
    /**
     * Construit le message de résultat de la progression
     * @param nextAcademicYear L'année académique suivante
     * @param createdCount Nombre d'années scolaires créées
     * @param archivedCount Nombre d'étudiants archivés
     * @param alreadyHasNextYearCount Nombre d'étudiants ayant déjà une année suivante
     * @return Le message formaté
     */
    private String buildProgressionMessage(String nextAcademicYear, int createdCount, 
                                          int archivedCount, int alreadyHasNextYearCount) {
        StringBuilder message = new StringBuilder();
        message.append("Passage à l'année ").append(nextAcademicYear).append(" effectué avec succès. ");
        message.append(createdCount).append(" année(s) scolaire(s) créée(s)");
        
        if (archivedCount > 0) {
            message.append(", ").append(archivedCount).append(" étudiant(s) archivé(s)");
        }
        
        if (alreadyHasNextYearCount > 0) {
            message.append(", ").append(alreadyHasNextYearCount)
                   .append(" étudiant(s) avai(en)t déjà une année pour ")
                   .append(nextAcademicYear);
        }
        
        message.append(".");
        return message.toString();
    }
    
    /**
     * Détermine le programme suivant
     * @param currentProgram Le programme actuel
     * @return Le programme suivant
     */
    private Program getNextProgram(Program currentProgram) {
        return switch (currentProgram) {
            case L1_APP -> Program.L2_APP;
            case L2_APP -> Program.L3_APP;
            case L3_APP -> Program.M1_APP;
            case M1_APP -> Program.M2_APP;
            case M2_APP -> Program.M2_APP;
        };
    }

    @Override
    public List<StudentDTO> getAllArchivedStudents() {
        try {
            List<Student> archivedStudents = studentRepository.findAllArchivedStudents();
            return archivedStudents.isEmpty() ? List.of() : studentMapper.toDTOList(archivedStudents);
        } catch (DataAccessException ex) {
            logger.error("Erreur d'accès aux données lors de la récupération des étudiants archivés", ex);
            throw new DatabaseException(
                "Une erreur de base de données est survenue lors de la récupération des étudiants archivés", ex);
        }
    }

    @Override
    @Transactional
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
            .orElseThrow(() -> {
                logger.error("Aucun étudiant trouvé avec l'ID : {}", id);
                return new ResourceNotFoundException(
                    String.format("L'étudiant avec l'identifiant %d est introuvable", id));
            });
        
        try {
            studentRepository.delete(student);
            logger.info("Étudiant supprimé : {} {}", student.getFirstName(), student.getLastName());
        } catch (DataAccessException ex) {
            logger.error("Erreur d'accès aux données lors de la suppression de l'étudiant avec l'ID : {}", id, ex);
            throw new DatabaseException(
                "Une erreur de base de données est survenue lors de la suppression de l'étudiant", ex);
        }
    }

    @Override
    @Transactional
    public void unarchiveStudent(Long id) {
        Student student = studentRepository.findById(id)
            .orElseThrow(() -> {
                logger.error("Aucun étudiant trouvé avec l'ID : {}", id);
                return new ResourceNotFoundException(
                    String.format("L'étudiant avec l'identifiant %d est introuvable", id));
            });
        
        try {
            student.setIsArchived(false);
            studentRepository.save(student);
            logger.info("Étudiant désarchivé : {} {}", student.getFirstName(), student.getLastName());
        } catch (DataAccessException ex) {
            logger.error("Erreur d'accès aux données lors du désarchivage de l'étudiant avec l'ID : {}", id, ex);
            throw new DatabaseException(
                "Une erreur de base de données est survenue lors du désarchivage de l'étudiant", ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentDTO> searchStudents(SearchCriteriaDTO criteria) {
        try {
            logger.info("Recherche d'étudiants avec les critères : nom={}, entreprise={}, mission={}, année={}",
                criteria.getName(), criteria.getCompany(), criteria.getMissionKeyword(), criteria.getAcademicYear());
            
            List<Student> students = studentRepository.searchStudents(
                    nullIfEmpty(criteria.getName()),
                    nullIfEmpty(criteria.getCompany()),
                    nullIfEmpty(criteria.getMissionKeyword()),
                    nullIfEmpty(criteria.getAcademicYear())
            );
            
            students.forEach(student -> student.getSchoolYears().size());
            
            logger.info("Recherche terminée : {} étudiant(s) trouvé(s)", students.size());
            return students.isEmpty() ? List.of() : studentMapper.toDTOList(students);
            
        } catch (DataAccessException ex) {
            logger.error("Erreur d'accès aux données lors de la recherche des étudiants", ex);
            throw new DatabaseException(
                "Une erreur de base de données est survenue lors de la recherche des étudiants", ex);
        }
    }

    /**
     * Convertit une chaîne vide en null pour les requêtes JPA
     * @param value La valeur à convertir
     * @return null si la chaîne est vide ou ne contient que des espaces, la valeur sinon
     */
    private String nullIfEmpty(String value) {
        return (value == null || value.trim().isEmpty()) ? null : value;
    }

    @Override
    @Transactional
    public void updateStudentBasicInfo(Long studentId, StudentUpdateDTO dto) {
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> {
                logger.error("Aucun étudiant trouvé avec l'ID : {}", studentId);
                return new ResourceNotFoundException(
                    String.format("L'étudiant avec l'identifiant %d est introuvable", studentId));
            });
        
        // L'email sera vérifié par la contrainte de base de données
        
        try {
            student.setFirstName(dto.getFirstName());
            student.setLastName(dto.getLastName());
            student.setEmail(dto.getEmail());
            student.setPhone(dto.getPhone());
            
            studentRepository.save(student);
            logger.info("Informations de base mises à jour pour l'étudiant {} {}", 
                dto.getFirstName(), dto.getLastName());
            
        } catch (DataAccessException ex) {
            logger.error("Erreur d'accès aux données lors de la mise à jour de l'étudiant avec l'ID : {}", studentId, ex);
            throw new DatabaseException(
                "Une erreur de base de données est survenue lors de la mise à jour de l'étudiant", ex);
        }
    }
}
