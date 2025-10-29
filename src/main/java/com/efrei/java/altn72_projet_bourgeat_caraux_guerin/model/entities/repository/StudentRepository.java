package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.repository;

import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

/**
 * Repository pour l'étudiant
 */
public interface StudentRepository extends JpaRepository<Student, Long> {

    /**
     * Récupère un étudiant par son email
     * @param email L'email de l'étudiant
     * @return L'étudiant s'il existe
     */
    Optional<Student> findByEmail(String email);

    /**
     * Récupère la liste des étudiants non archivés pour une année scolaire spécifique
     * @param academicYear L'année scolaire
     * @return La liste des étudiants
     */
    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.schoolYears sy WHERE sy.academicYear = :academicYear AND (s.isArchived IS NULL OR s.isArchived = false)")
    List<Student> findBySchoolYears_AcademicYear(@Param("academicYear") String academicYear);

    /**
     * Récupère la liste de tous les étudiants archivés
     * @return La liste des étudiants archivés
     */
    @Query("SELECT DISTINCT s FROM Student s LEFT JOIN FETCH s.schoolYears WHERE s.isArchived = true ORDER BY s.lastName, s.firstName")
    List<Student> findAllArchivedStudents();

    /**
     * Recherche des étudiants selon plusieurs critères
     * @param name Nom de l'étudiant
     * @param company Nom de l'entreprise
     * @param missionKeyword Mot-clé dans la mission
     * @param academicYear Année académique
     * @return La liste des étudiants correspondant aux critères
     */
    @Query("""
        SELECT DISTINCT s
        FROM Student s
        LEFT JOIN s.schoolYears sy
        LEFT JOIN sy.company c
        LEFT JOIN sy.mission m
        WHERE (s.isArchived IS NULL OR s.isArchived = false)
          AND (:name IS NULL OR LOWER(s.lastName) LIKE LOWER(CONCAT('%', :name, '%')))
          AND (:company IS NULL OR (c IS NOT NULL AND LOWER(c.companyName) LIKE LOWER(CONCAT('%', :company, '%'))))
          AND (:missionKeyword IS NULL OR (m IS NOT NULL AND LOWER(m.keywords) LIKE LOWER(CONCAT('%', :missionKeyword, '%'))))
          AND (:academicYear IS NULL OR (sy IS NOT NULL AND sy.academicYear = :academicYear))
    """)
    List<Student> searchStudents(
            @Param("name") String name,
            @Param("company") String company,
            @Param("missionKeyword") String missionKeyword,
            @Param("academicYear") String academicYear
    );
}
