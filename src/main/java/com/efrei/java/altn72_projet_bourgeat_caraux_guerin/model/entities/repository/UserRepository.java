package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.repository;

import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository pour l'utilisateur
 */
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Récupère un utilisateur par son nom d'utilisateur
     * @param username Le nom d'utilisateur
     * @return L'utilisateur si trouvé
     */
    Optional<User> findByUsername(String username);
}
