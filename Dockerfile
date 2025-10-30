# Stage 1: Build
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copier seulement pom.xml pour télécharger les dépendances (cache layer)
COPY pom.xml .

# Télécharger les dépendances (ce layer sera en cache si pom.xml ne change pas)
RUN mvn dependency:go-offline -B || true

# Copier le code source
COPY src ./src

# Compiler l'application
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copier le JAR depuis le stage de build
COPY --from=build /app/target/*.jar app.jar

# Exposer le port
EXPOSE 8080

# Variable d'environnement pour le profil Spring (peut être écrasée)
ENV SPRING_PROFILES_ACTIVE=prod

# Lancer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]

