# Rapport de Projet - Gestion des Alternants

**Auteurs :** BOURGEAT Bastien, CARAUX Ghislain, GUERIN Nam
**Cours :** ALTN72 - Développement Java Avancé

---

## 1. Identifiants de Connexion

Pour tester l'application, utilisez les identifiants suivants :

- **Username :** `jean.dupont`
- **Mot de passe :** `motdepasse`

## 2. Informations sur l'Outillage

### 2.1 IDE Utilisé

Nous avons utilisé **IntelliJ IDEA** pour le développement de l'application.

### 2.2 Système de Gestion de Base de Données

En développement local : **MariaDB 10.4.28**

En production : **PostgreSQL 16** pour Render.com

Le choix de PostgreSQL en production a été fait pour profiter de l'offre gratuite de Render.com qui fournit une base de données persistante.

## 3. Instructions pour Lancer et Tester l'Application

### 3.1 Prérequis

- Java 17
- Maven 3.6+
- MariaDB 10.4+ (en local)

### 3.2 Configuration de la Base de Données

1. Créez une base de données MariaDB nommée `projet_java`
2. Exécutez le script SQL fourni dans `schema.sql` si nécessaire
3. Vérifiez les paramètres de connexion dans `src/main/resources/application.properties`

### 3.3 Lancement de l'Application

```bash
# Compiler le projet
./mvnw clean package

# Lancer l'application
./mvnw spring-boot:run
```

L'application sera accessible sur `http://localhost:8080`

### 3.4 Accès à la Documentation API

La documentation Swagger est accessible à l'adresse : `http://localhost:8080/swagger-ui.html`

### 3.5 Application Déployée

L'application est déployée et accessible en ligne via Render.com. L'URL est configurée dans le fichier `render.yaml`.

### 3.6 Tests

Pour exécuter les tests unitaires :

```bash
./mvnw test
```

Pour générer le rapport de couverture JaCoCo :

```bash
./mvnw test jacoco:report
```

Le rapport sera disponible dans `target/site/jacoco/index.html`

## 4. Réponses aux Questions

### a) Sur quel aspect de votre travail souhaitez-vous attirer l'attention du correcteur ?

Nous souhaitons mettre en avant plusieurs points :

1. **Architecture en couches bien structurée** : Nous avons respecté une séparation claire entre les contrôleurs, les services, les repositories et les DTOs. Notamment avec l'utilisation des interfaces.

2. **Gestion complète du cycle de vie des étudiants** : L'application gère non seulement les étudiants actifs, mais aussi leur archivage, avec un historique complet de leurs années scolaires, missions, rapports et visites.

3. **Fonctionnalité de recherche avancée** : Nous avons implémenté une recherche multicritères permettant de filtrer les étudiants par nom, entreprise, mots-clés de mission et année académique.

4. **Interface utilisateur moderne et responsive** : L'interface utilise un design moderne avec une navigation intuitive, des formulaires bien structurés et des éléments visuels clairs.

### b) Quelle est la plus grande difficulté que vous avez rencontrée ?

La plus grande difficulté a été la gestion des relations bidirectionnelles entre les entités JPA, notamment entre `Student`, `SchoolYear`, et les différentes entités liées (`Company`, `Mentor`, `Visit`).

Pour solutionner ce problème, nous avons :

1. Utilisé les annotations `@JsonIgnore` et `@JsonBackReference` pour éviter les boucles infinies lors de la sérialisation JSON
2. Mis en place des DTOs avec MapStruct pour contrôler précisément quelles données sont exposées par l'API
3. Ajouté des requêtes JPQL avec `LEFT JOIN FETCH` pour optimiser le chargement des relations et éviter le problème N+1
4. Utilisé `@Transactional` de manière stratégique pour gérer les opérations en cascade lors de la création et modification des entités

### c) Quelle a été la contribution de chaque membre de l'équipe ?

**BOURGEAT Bastien :**

- Mise en place de l'architecture de base (entités, repositories, services)
- Implémentation de la sécurité avec Spring Security
- Configuration du déploiement sur Render.com
- Gestion des exceptions et des réponses HTTP
- Implémentation des tests unitaires

**CARAUX :**

- Développement des contrôleurs et de la logique métier
- Création des DTOs et des mappers avec MapStruct
- Implémentation des fonctionnalités de recherche et de filtrage
- Documentation du code

**GUERIN :**

- Développement de l'interface utilisateur avec Thymeleaf
- Intégration des templates HTML/CSS
- Création des formulaires et validation côté client
- Tests fonctionnels de l'interface

### d) Si vous deviez retenir trois points de ce cours en général et de ce projet en particulier, quels seraient ces trois points ?

1. **L'importance de la séparation des responsabilités** : La structure en couches (Controller, Service, Repository) rend le code maintenable et testable. Chaque composant a une responsabilité claire, ce qui facilite la compréhension et l'évolution du code.

2. **La puissance de Spring Boot et de son écosystème** : Spring Boot simplifie énormément le développement d'applications d'entreprise. La configuration automatique, l'injection de dépendances et l'intégration avec JPA, Security, et Thymeleaf permettent de se concentrer sur la logique métier plutôt que sur la configuration.

3. **L'importance des bonnes pratiques de gestion d'erreurs** : Une gestion d'erreurs appropriée améliore considérablement l'expérience utilisateur. L'utilisation de `ResponseStatusException` avec des messages personnalisés en français permet de fournir des retours clairs et compréhensibles.

### e) Les fonctionnalités que vous n'avez pas eu le temps de mettre en œuvre et pourquoi

Plusieurs fonctionnalités auraient pu être ajoutées avec plus de temps :

1. **Export et import de données** : Permettre l'export des données en PDF ou Excel pour générer des rapports. Cela aurait nécessité l'intégration de bibliothèques supplémentaires comme Apache POI ou iText.

2. **Système de gestion des rôles avancé** : Actuellement, tous les utilisateurs authentifiés ont les mêmes droits. Il aurait été intéressant d'implémenter différents rôles (administrateur, tuteur simple) avec des permissions différentes.

3. **Upload de fichiers** : Permettre aux étudiants d'uploader leurs rapports directement dans l'application. Cette fonctionnalité a été mise de côté par manque de temps et pour éviter les problèmes de stockage en production.

4. **Tableau de bord avec statistiques avancées** : Des graphiques plus élaborés (évolution des notes, répartition géographique des entreprises, etc.) auraient pu enrichir le tableau de bord.

### f) À quel niveau, dans votre projet, avez-vous réussi à respecter entièrement ou partiellement les principes SOLID ?

#### S - Single Responsibility Principle (Principe de responsabilité unique)

Chaque classe a une responsabilité unique et bien définie :

- Les `Controllers` gèrent uniquement les requêtes HTTP et délèguent la logique métier aux services
- Les `Services` contiennent la logique métier
- Les `Repositories` gèrent uniquement l'accès aux données
- Les `Mappers` s'occupent uniquement de la conversion entre entités et DTOs

Exemple : `CompanyServiceImpl` gère uniquement la logique métier des entreprises, sans se préoccuper de la présentation ou de la persistance.

#### O - Open/Closed Principle (Principe ouvert/fermé)

L'utilisation d'interfaces pour les services (`CompanyService`, `StudentService`, etc.) permet d'étendre le comportement sans modifier le code existant.

#### L - Liskov Substitution Principle (Principe de substitution de Liskov)

Les implémentations de services peuvent être substituées par leurs interfaces sans altérer le comportement de l'application. L'injection de dépendances de Spring garantit ce principe.

Exemple : `CompanyServiceImpl` peut être remplacée par une autre implémentation de `CompanyService` sans impacter les contrôleurs.

#### I - Interface Segregation Principle (Principe de ségrégation des interfaces)

Nos interfaces de services sont spécifiques et ne forcent pas les classes à implémenter des méthodes inutiles. Chaque service a une interface dédiée avec uniquement les méthodes nécessaires.

Exemple : `CompanyService` ne contient que des méthodes liées aux entreprises, et `StudentService` uniquement des méthodes liées aux étudiants.

#### D - Dependency Inversion Principle (Principe d'inversion des dépendances)

Les contrôleurs et services dépendent d'abstractions (interfaces) et non d'implémentations concrètes. L'injection de dépendances par constructeur de Spring garantit ce principe.

Exemple : Les contrôleurs dépendent de `CompanyService` (interface) et non de `CompanyServiceImpl` (implémentation concrète).

## 5. Respect des Exigences Techniques

### 5.1 Utilisation de Spring Boot

L'application utilise Spring Boot 3.5.6 avec les starters suivants :

- `spring-boot-starter-web` pour l'API REST et les contrôleurs
- `spring-boot-starter-data-jpa` pour la persistance
- `spring-boot-starter-thymeleaf` pour les vues
- `spring-boot-starter-security` pour l'authentification
- `spring-boot-starter-validation` pour la validation des données

### 5.2 Stack Technique

La stack utilisée est identique à celle du TP fil rouge :

- **Backend :** Spring Boot, Spring Data JPA, Spring Security
- **Frontend :** Thymeleaf, Bootstrap, JavaScript
- **Base de données :** MariaDB (dev) / PostgreSQL (prod)
- **Build :** Maven
- **Tests :** JUnit 5, Mockito
- **Autres :** Lombok, MapStruct, SpringDoc OpenAPI

### 5.3 Documentation API avec Swagger

La documentation API est disponible via SpringDoc OpenAPI (Swagger UI). La configuration se trouve dans `OpenApiConfig.java`. L'interface Swagger est accessible à l'adresse `/swagger-ui.html`.

### 5.4 Déploiement

L'application est déployée sur **Render.com** grâce au fichier de configuration `render.yaml`. Elle utilise :

- Un service web avec Docker (défini dans `Dockerfile`)
- Une base de données PostgreSQL gratuite
- Une configuration de production dans `application-prod.properties`

### 5.5 Gestion des Exceptions

Nous avons choisi d'utiliser **ResponseStatusException** plutôt que `@ControllerAdvice` parce que chaque exception est levée avec un message spécifique en français

#### 5.6 Requête SQL Native - Analyse Critique

Nous avons utilisé une requête SQL native dans `SchoolYearRepository` :

```java
@Query(value = "SELECT DISTINCT academic_year FROM school_year ORDER BY academic_year DESC", nativeQuery = true)
List<String> findDistinctAcademicYearByOrderByAcademicYearDesc();
```

**Analyse critique :**

**Avantages :**

- Performance optimale pour cette requête simple
- Plus concise que l'équivalent JPQL
- Évite de charger des objets complets quand on a juste besoin d'une colonne

**Inconvénients :**

- Perte de la portabilité entre SGBD
- Pas de vérification de type à la compilation
- Si la structure de la table change (nom de colonne), le code ne sera pas mis à jour automatiquement par les outils de refactoring

**Conclusion :**
Pour la majorité des cas, JPQL reste préférable car il offre une meilleure maintenabilité et portabilité.

#### c) Utilisation de @Transactional

Nous avons utilisé `@Transactional` dans les services, principalement pour :

**Justification :**

- **Cohérence des données** : En cas d'erreur, toutes les modifications sont annulées (rollback)
- **Performance** : Une seule connexion à la base de données pour plusieurs requêtes
- **Lazy Loading** : Permet le chargement des relations à la demande dans les méthodes de service

- Nous n'avons pas annoté les méthodes de lecture simple avec `@Transactional` parce que Spring le fait déjà implicitement.

## 7. Conclusion

Ce projet nous a permis de mettre en pratique les concepts avancés de Spring Boot dans un contexte réel de gestion d'alternants. Nous avons réussi à développer une application complète, testée et déployée en production, en respectant les bonnes pratiques de développement et les principes SOLID.
