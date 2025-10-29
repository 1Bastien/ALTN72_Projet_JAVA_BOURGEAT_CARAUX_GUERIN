package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.service;

import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.User;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service d'authentification pour la gestion des utilisateurs
 */
@Service
public class AuthService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Méthode utilisée lors de la connexion, récupère l'utilisateur par son username
     * @param username Le nom d'utilisateur
     * @return L'utilisateur (qui implémente UserDetails)
     * @throws UsernameNotFoundException Si l'utilisateur n'est pas trouvé
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> {
                logger.error("Échec de connexion : utilisateur '{}' non trouvé", username);
                return new UsernameNotFoundException("Utilisateur non trouvé : " + username);
            });

        logger.info("Connexion réussie pour l'utilisateur : {} {} ({})", 
            user.getFirstName(), user.getLastName(), user.getUsername());
        
        return user;
    }
}

