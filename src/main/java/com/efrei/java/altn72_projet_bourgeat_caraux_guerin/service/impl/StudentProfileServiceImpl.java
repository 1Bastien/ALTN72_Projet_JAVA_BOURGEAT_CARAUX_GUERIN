package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.service.impl;

import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto.SchoolYearUpdateDTO;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.*;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.repository.CompanyRepository;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.repository.MentorRepository;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.repository.SchoolYearRepository;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.repository.StudentRepository;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.repository.UserRepository;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.enums.Format;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.enums.Program;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.mapper.SchoolYearMapper;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.service.*;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.utils.AcademicYearUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implémentation du service pour la gestion de la page de profil d'un étudiant
 */
@Service
public class StudentProfileServiceImpl implements StudentProfileService {

    private static final Logger logger = LoggerFactory.getLogger(StudentProfileServiceImpl.class);

    private final StudentRepository studentRepository;
    private final SchoolYearRepository schoolYearRepository;
    private final CompanyRepository companyRepository;
    private final MentorRepository mentorRepository;
    private final CompanyService companyService;
    private final MentorService mentorService;
    private final VisitService visitService;
    private final UserRepository userRepository;
    private final SchoolYearMapper schoolYearMapper;

    public StudentProfileServiceImpl(StudentRepository studentRepository, 
                                    SchoolYearRepository schoolYearRepository,
                                    CompanyRepository companyRepository,
                                    MentorRepository mentorRepository,
                                    CompanyService companyService,
                                    MentorService mentorService,
                                    VisitService visitService,
                                    UserRepository userRepository,
                                    SchoolYearMapper schoolYearMapper) {
        this.studentRepository = studentRepository;
        this.schoolYearRepository = schoolYearRepository;
        this.companyRepository = companyRepository;
        this.mentorRepository = mentorRepository;
        this.companyService = companyService;
        this.mentorService = mentorService;
        this.visitService = visitService;
        this.userRepository = userRepository;
        this.schoolYearMapper = schoolYearMapper;
    }

    @Override
    public void prepareProfileModel(Model model, Long studentId, String academicYear) {
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                String.format("L'étudiant avec l'identifiant %d est introuvable", studentId)));

        try {
            List<SchoolYear> studentSchoolYears = schoolYearRepository.findByStudentId(studentId);
            List<String> academicYears = studentSchoolYears.stream()
                .map(SchoolYear::getAcademicYear)
                .distinct()
                .sorted((a, b) -> b.compareTo(a))
                .toList();

            String displayYear = academicYear;
            if (displayYear == null || displayYear.isEmpty()) {
                displayYear = AcademicYearUtils.findClosestAcademicYear(academicYears);
            }

            SchoolYear currentSchoolYear = schoolYearRepository
                .findByStudentIdAndAcademicYear(studentId, displayYear)
                .orElse(null);

            model.addAttribute("student", student);
            model.addAttribute("academicYears", academicYears);
            model.addAttribute("currentYear", displayYear);
            model.addAttribute("schoolYear", currentSchoolYear);
            model.addAttribute("isDashboard", false);

            model.addAttribute("companies", companyService.getAllCompanies());
            model.addAttribute("mentors", mentorService.getAllMentors());
            model.addAttribute("tutors", userRepository.findAll());
            model.addAttribute("programs", Program.values());
            model.addAttribute("formats", Format.values());

            if (currentSchoolYear != null) {
                model.addAttribute("visits", visitService.getVisitsBySchoolYearId(currentSchoolYear.getId()));
            } else {
                model.addAttribute("visits", List.of());
            }

            logger.info("Modèle de profil préparé pour l'étudiant {} - année {}", studentId, displayYear);
        } catch (DataAccessException ex) {
            logger.error("Erreur d'accès aux données lors de la préparation du modèle de profil pour l'étudiant {}", 
                studentId, ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                "Une erreur de base de données est survenue lors de la préparation de la page de profil");
        }
    }

    @Override
    @Transactional
    public SchoolYear updateSchoolYear(Long schoolYearId, SchoolYearUpdateDTO dto) {
        SchoolYear schoolYear = getSchoolYearById(schoolYearId);
        
        if (dto.getCompanyId() != null) {
            Company company = getCompanyEntityById(dto.getCompanyId());
            schoolYear.setCompany(company);
        }
        
        if (dto.getMentorId() != null) {
            Mentor mentor = getMentorEntityById(dto.getMentorId());
            schoolYear.setMentor(mentor);
        }
        
        try {
            schoolYearMapper.updateFromDTO(dto, schoolYear);
            
            if (dto.getMissionKeywords() != null || dto.getMissionTargetJob() != null || dto.getMissionComment() != null) {
                if (schoolYear.getMission() == null) {
                    schoolYear.setMission(new Mission());
                }
                if (dto.getMissionKeywords() != null) {
                    schoolYear.getMission().setKeywords(dto.getMissionKeywords());
                }
                if (dto.getMissionTargetJob() != null) {
                    schoolYear.getMission().setTargetJob(dto.getMissionTargetJob());
                }
                if (dto.getMissionComment() != null) {
                    schoolYear.getMission().setComment(dto.getMissionComment());
                }
            }
            
            if (dto.getReportSubject() != null || dto.getReportGrade() != null || dto.getReportComment() != null) {
                if (schoolYear.getReport() == null) {
                    schoolYear.setReport(new Report());
                }
                if (dto.getReportSubject() != null) {
                    schoolYear.getReport().setSubject(dto.getReportSubject());
                }
                if (dto.getReportGrade() != null) {
                    schoolYear.getReport().setReportGrade(dto.getReportGrade());
                }
                if (dto.getReportComment() != null) {
                    schoolYear.getReport().setComment(dto.getReportComment());
                }
            }
            
            if (dto.getPresentationDate() != null || dto.getPresentationTime() != null || 
                dto.getPresentationGrade() != null || dto.getPresentationComment() != null) {
                if (schoolYear.getPresentation() == null) {
                    schoolYear.setPresentation(new Presentation());
                }
                
                if (dto.getPresentationDate() != null && dto.getPresentationTime() != null) {
                    LocalDateTime presentationDateTime = LocalDateTime.of(
                        dto.getPresentationDate(), 
                        dto.getPresentationTime()
                    );
                    schoolYear.getPresentation().setDate(presentationDateTime);
                } else if (dto.getPresentationDate() != null) {
                    LocalDateTime presentationDateTime = dto.getPresentationDate().atStartOfDay();
                    schoolYear.getPresentation().setDate(presentationDateTime);
                }
                
                if (dto.getPresentationGrade() != null) {
                    schoolYear.getPresentation().setPresentationGrade(dto.getPresentationGrade());
                }
                if (dto.getPresentationComment() != null) {
                    schoolYear.getPresentation().setComment(dto.getPresentationComment());
                }
            }
            
            SchoolYear updated = schoolYearRepository.save(schoolYear);
            logger.info("Année scolaire {} mise à jour avec succès", schoolYearId);
            return updated;
            
        } catch (DataAccessException ex) {
            logger.error("Erreur d'accès aux données lors de la mise à jour de l'année scolaire {}", schoolYearId, ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                "Une erreur de base de données est survenue lors de la mise à jour de l'année scolaire");
        }
    }

    @Override
    public SchoolYear getSchoolYearByStudentIdAndAcademicYear(Long studentId, String academicYear) {
        return schoolYearRepository.findByStudentIdAndAcademicYear(studentId, academicYear)
            .orElseThrow(() -> {
                logger.error("Aucune année scolaire trouvée pour l'étudiant {} et l'année {}", studentId, academicYear);
                return new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Aucune année scolaire trouvée pour l'étudiant %d et l'année %s", studentId, academicYear));
            });
    }

    @Override
    public SchoolYear getSchoolYearById(Long schoolYearId) {
        return schoolYearRepository.findById(schoolYearId)
            .orElseThrow(() -> {
                logger.error("Aucune année scolaire trouvée avec l'ID : {}", schoolYearId);
                return new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("L'année scolaire avec l'identifiant %d est introuvable", schoolYearId));
            });
    }

    @Override
    public Company getCompanyEntityById(Long companyId) {
        return companyRepository.findById(companyId)
            .orElseThrow(() -> {
                logger.error("Aucune entreprise trouvée avec l'ID : {}", companyId);
                return new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("L'entreprise avec l'identifiant %d est introuvable", companyId));
            });
    }

    @Override
    public Mentor getMentorEntityById(Long mentorId) {
        return mentorRepository.findById(mentorId)
            .orElseThrow(() -> {
                logger.error("Aucun mentor trouvé avec l'ID : {}", mentorId);
                return new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Le mentor avec l'identifiant %d est introuvable", mentorId));
            });
    }
}

