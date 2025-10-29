package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.config;

import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.User;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Initialisation des données au démarrage de l'application
 */
@Configuration
public class DataInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    /**
     * Crée un utilisateur par défaut si aucun utilisateur n'existe dans la base de données
     * @param userRepository Le repository des utilisateurs
     * @param passwordEncoder L'encodeur de mot de passe
     * @return Le CommandLineRunner qui initialise les données
     */
    @Bean
    public CommandLineRunner initDefaultUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() == 0) {
                logger.info("Aucun utilisateur trouvé dans la base de données. Création d'un utilisateur par défaut...");
                
                User defaultUser = new User();
                defaultUser.setFirstName("Admin");
                defaultUser.setLastName("Tuteur");
                defaultUser.setUsername("admin");
                defaultUser.setPassword(passwordEncoder.encode("admin"));
                defaultUser.setEmail("admin@efrei.fr");
                
                userRepository.save(defaultUser);
                
                logger.info("✅ Utilisateur par défaut créé avec succès !");
                logger.info("   Username: admin");
                logger.info("   Password: admin");
                logger.info("   Vous pouvez maintenant vous connecter avec ces identifiants.");
            } else {
                logger.info("Des utilisateurs existent déjà dans la base de données. Aucune initialisation nécessaire.");
            }
        };
    }
}

