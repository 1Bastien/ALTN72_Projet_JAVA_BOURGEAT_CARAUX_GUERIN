package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.repository;

import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository pour l'utilisateur
 */
public interface UserRepository extends JpaRepository<User, Long> {
}
