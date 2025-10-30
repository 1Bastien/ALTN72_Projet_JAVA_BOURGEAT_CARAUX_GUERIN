package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

/**
 * Configuration pour corriger l'URL PostgreSQL fournie par Render
 * Render fournit l'URL au format: postgresql://user:password@host:port/db
 * Mais JDBC attend: jdbc:postgresql://host:port/db?user=user&password=password
 */
@Configuration
@Profile("prod")
public class DatabaseConfig {

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties dataSourceProperties() {
        DataSourceProperties properties = new DataSourceProperties();
        
        String databaseUrl = System.getenv("DATABASE_URL");
        
        if (databaseUrl != null && !databaseUrl.isEmpty()) {
            parseAndSetDatabaseUrl(databaseUrl, properties);
        }
        
        // Définir explicitement le driver PostgreSQL
        properties.setDriverClassName("org.postgresql.Driver");
        
        return properties;
    }

    @Bean
    @Primary
    public DataSource dataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }

    /**
     * Parse l'URL Render PostgreSQL et configure les propriétés de la DataSource
     * Input:  postgresql://user:pass@host:port/db
     */
    private void parseAndSetDatabaseUrl(String renderUrl, DataSourceProperties properties) {
        // Si c'est déjà une URL JDBC, l'utiliser directement
        if (renderUrl.startsWith("jdbc:")) {
            properties.setUrl(renderUrl);
            return;
        }

        if (renderUrl.startsWith("postgresql://") || renderUrl.startsWith("postgres://")) {
            String urlWithoutProtocol = renderUrl.replaceFirst("postgres(ql)?://", "");
            
            String[] parts = urlWithoutProtocol.split("@", 2);
            if (parts.length == 2) {
                // Extraire les credentials
                String[] credentials = parts[0].split(":", 2);
                String username = credentials[0];
                String password = credentials.length > 1 ? credentials[1] : "";
                
                // Construire l'URL JDBC sans les credentials dans l'URL
                String jdbcUrl = "jdbc:postgresql://" + parts[1];
                
                // Configurer les propriétés séparément
                properties.setUrl(jdbcUrl);
                properties.setUsername(username);
                properties.setPassword(password);
            }
        }
    }
}

