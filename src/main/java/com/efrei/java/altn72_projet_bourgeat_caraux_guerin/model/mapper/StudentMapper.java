package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.mapper;

import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto.StudentDTO;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.Student;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper pour l'étudiant
 */
@Mapper(componentModel = "spring", uses = SchoolYearMapper.class)
public interface StudentMapper {

    /**
     * Convertit un étudiant en DTO
     * @param student L'étudiant
     * @return Le DTO
     */
    StudentDTO toDTO(Student student);

    /**
     * Convertit un DTO en étudiant (sans la relation SchoolYear)
     * @param studentDTO Le DTO
     * @return L'étudiant
     */
    @Mapping(target = "schoolYears", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isArchived", ignore = true)
    Student toEntity(StudentDTO studentDTO);

    /**
     * Convertit une liste d'étudiants en liste de DTO
     * @param students La liste d'étudiants
     * @return La liste de DTO
     */
    List<StudentDTO> toDTOList(List<Student> students);
}
