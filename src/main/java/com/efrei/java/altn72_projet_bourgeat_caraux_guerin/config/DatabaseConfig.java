package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * Configuration de la base de données pour Render.com
 * Parse automatiquement DATABASE_URL fournie par Render au format postgres://user:password@host:port/database
 * et la convertit au format JDBC attendu par Spring Boot
 * 
 * Cette configuration est active uniquement en profil 'prod' (production sur Render.com)
 */
@Configuration
@Profile("prod")
public class DatabaseConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);

    @Bean
    public DataSource dataSource() {
        String databaseUrl = System.getenv("DATABASE_URL");
        
        if (databaseUrl == null || databaseUrl.isEmpty()) {
            logger.error("DATABASE_URL n'est pas définie dans les variables d'environnement");
            throw new IllegalStateException("DATABASE_URL est requise en mode production");
        }
        
        try {
            URI dbUri = new URI(databaseUrl);
            
            if (dbUri.getUserInfo() == null || !dbUri.getUserInfo().contains(":")) {
                throw new IllegalStateException("DATABASE_URL mal formatée : informations d'authentification manquantes");
            }
            
            String[] userInfo = dbUri.getUserInfo().split(":", 2);
            String username = URLDecoder.decode(userInfo[0], StandardCharsets.UTF_8);
            String password = URLDecoder.decode(userInfo[1], StandardCharsets.UTF_8);
            String host = dbUri.getHost();
            int port = dbUri.getPort() != -1 ? dbUri.getPort() : 5432;
            String database = dbUri.getPath().substring(1);
            
            String jdbcUrl = String.format("jdbc:postgresql://%s:%d/%s?sslmode=require", host, port, database);
            
            logger.info("Configuration base de données Render détectée");
            logger.info("   Host: {}", host);
            logger.info("   Port: {}", port);
            logger.info("   Database: {}", database);
            logger.info("   User: {}", username);
            
            return DataSourceBuilder.create()
                    .url(jdbcUrl)
                    .username(username)
                    .password(password)
                    .driverClassName("org.postgresql.Driver")
                    .build();
                    
        } catch (URISyntaxException e) {
            logger.error("Erreur lors du parsing de DATABASE_URL", e);
            throw new IllegalStateException("DATABASE_URL mal formatée: " + e.getMessage(), e);
        }
    }
}
