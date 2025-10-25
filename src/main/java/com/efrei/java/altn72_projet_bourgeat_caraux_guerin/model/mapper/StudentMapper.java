package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.mapper;

import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto.StudentDTO;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.Student;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.SchoolYear;

/**
 * Mapper pour l'étudiant
 */
@Mapper(componentModel = "spring", uses = SchoolYearMapper.class)
public abstract class StudentMapper {

    @Autowired
    protected SchoolYearMapper schoolYearMapper;

    /**
     * Convertit un étudiant en DTO
     * @param student L'étudiant
     * @return Le DTO
     */
    @Mapping(target = "schoolYear", expression = "java(student.getSchoolYears() != null && !student.getSchoolYears().isEmpty() ? schoolYearMapper.toDTO(student.getSchoolYears().get(0)) : null)")
    public abstract StudentDTO toDTO(Student student);

    /**
     * Convertit un DTO en étudiant et l'associe à une année scolaire
     * @param studentDTO Le DTO
     * @return L'étudiant
     */
    public Student toEntity(StudentDTO studentDTO) {
        Student student = new Student();
        BeanUtils.copyProperties(studentDTO, student, "schoolYear");
        
        if (studentDTO.getSchoolYear() != null) {
            SchoolYear schoolYear = schoolYearMapper.toEntity(studentDTO.getSchoolYear());
            schoolYear.setStudent(student);
            student.setSchoolYears(List.of(schoolYear));
        }
        
        return student;
    }

    /**
     * Convertit une liste d'étudiants en liste de DTO
     * @param students La liste d'étudiants
     * @return La liste de DTO
     */
    public abstract List<StudentDTO> toDTOList(List<Student> students);
}
