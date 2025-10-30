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
            String jdbcUrl = convertRenderUrlToJdbc(databaseUrl);
            properties.setUrl(jdbcUrl);
        }
        
        return properties;
    }

    @Bean
    @Primary
    public DataSource dataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }

    /**
     * Convertit une URL Render PostgreSQL en URL JDBC
     * Input:  postgresql://user:pass@host:port/db
     * Output: jdbc:postgresql://host:port/db?user=user&password=pass
     */
    private String convertRenderUrlToJdbc(String renderUrl) {
        if (renderUrl.startsWith("jdbc:")) {
            return renderUrl;
        }

        if (renderUrl.startsWith("postgresql://") || renderUrl.startsWith("postgres://")) {
            String urlWithoutProtocol = renderUrl.replaceFirst("postgres(ql)?://", "");
            
            String[] parts = urlWithoutProtocol.split("@", 2);
            if (parts.length == 2) {
                String[] credentials = parts[0].split(":", 2);
                String user = credentials[0];
                String password = credentials.length > 1 ? credentials[1] : "";
                
                return "jdbc:postgresql://" + parts[1] + "?user=" + user + "&password=" + password;
            }
        }

        return renderUrl;
    }
}

