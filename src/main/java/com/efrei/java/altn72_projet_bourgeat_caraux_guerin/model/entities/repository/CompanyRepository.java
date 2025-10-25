package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.repository;

import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository pour l'entreprise
 */
public interface CompanyRepository extends JpaRepository<Company, Long> {
}
