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

En développement et en production : **PostgreSQL 16**

Le choix de PostgreSQL pour l'ensemble du projet assure une cohérence entre les environnements de développement et de production, et permet de profiter de l'offre gratuite de Render.com qui fournit une base de données persistante.

## 3. Instructions pour Lancer et Tester l'Application

### 3.1 Prérequis

- Java 17
- Maven 3.6+
- PostgreSQL 16+

### 3.2 Configuration de la Base de Données

1. Créez une base de données PostgreSQL nommée `projet_java`
2. Assurez-vous qu'un utilisateur PostgreSQL a accès à cette base (par défaut : `projet_java_user` / mot de passe : `password`)
3. Les tables seront automatiquement créées au premier démarrage grâce à Hibernate
4. Les données de test seront automatiquement initialisées grâce à `DataInitializer`

**Note :** Vous pouvez modifier les paramètres de connexion dans `src/main/resources/application.properties` si nécessaire.

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

2. **Interface utilisateur moderne**

### b) Quelle est la plus grande difficulté que vous avez rencontrée ?

La plus grande difficulté a été de concevoir une architecture de base de données adaptée à notre besoin métier. Au départ, nous envisagions de créer une table par section du fichier Excel fourni dans le sujet (une table pour les missions, une pour les rapports, une pour les présentations, etc.), ce qui aurait conduit à une structure fragmentée avec de nombreuses relations complexes entre tables.

Cependant, après réflexion et analyse du domaine, l'idée de centraliser toutes ces informations dans une entité `SchoolYear` (année scolaire) nous est apparue comme une solution bien plus élégante. Cette approche présente plusieurs avantages :

1. **Centralisation naturelle** : Une année scolaire regroupe logiquement toutes les informations d'un étudiant pour une année donnée (mission, rapport, présentation, visites, entreprise, etc.)
2. **Évolutivité** : Un étudiant peut facilement avoir plusieurs années scolaires, permettant de suivre son parcours complet
3. **Simplicité des relations** : Au lieu d'avoir de multiples relations entre `Student` et chaque entité (Mission, Report, Presentation), nous avons une seule relation `Student` → `SchoolYear` qui contient tout
4. **Cohérence métier** : Cette modélisation reflète mieux la réalité : chaque année scolaire est une entité cohérente avec ses propres caractéristiques

Cette décision architecturale, bien que difficile à prendre initialement, s'est révélée être la clé d'une application maintenable et extensible.

### c) Quelle a été la contribution de chaque membre de l'équipe ?

**BOURGEAT Bastien :**

- Mise en place de l'architecture de base (entités, repositories, services)

- Configuration du déploiement sur Render.com
- Gestion des exceptions et des réponses HTTP
- Implémentation des tests unitaires
- Création des DTOs et des mappers avec MapStruct
- Création de la logique métier
- Mise en place des bonnes pratiques et vérification de la qualité du code

**CARAUX Ghislain :**

- Implémentation des fonctionnalités de recherche et de filtrage
- Documentation du code
- Intégration des templates HTML/CSS
- Création de la logique métier

**GUERIN Nam :**

- Développement de l'interface utilisateur avec Thymeleaf
- Implémentation de la sécurité avec Spring Security
- Intégration des templates HTML/CSS
- Création des formulaires et validation côté client
- Création de la logique métier

### d) Si vous deviez retenir trois points de ce cours en général et de ce projet en particulier, quels seraient ces trois points ?

1. **L'importance de la séparation des responsabilités** : La structure en couches (Controller, Service, Repository) rend le code maintenable et testable. Chaque composant a une responsabilité claire, ce qui facilite la compréhension et l'évolution du code.

2. **La puissance de Spring Boot et de son écosystème** : Spring Boot simplifie énormément le développement d'applications d'entreprise. La configuration automatique, l'injection de dépendances et l'intégration avec JPA, Security, et Thymeleaf permettent de se concentrer sur la logique métier plutôt que sur la configuration.

3. **L'importance des bonnes pratiques de gestion d'erreurs** : Une gestion d'erreurs appropriée améliore considérablement l'expérience utilisateur. L'utilisation d'un gestionnaire global avec `@ControllerAdvice` et des exceptions personnalisées permet de fournir des retours clairs et compréhensibles en français, tout en gérant de manière appropriée les redirections avec messages flash pour l'interface web.

### e) Les fonctionnalités que vous n'avez pas eu le temps de mettre en œuvre et pourquoi

Plusieurs fonctionnalités auraient pu être ajoutées avec plus de temps :

1. **Export et import de données** : Permettre l'export des données en PDF ou Excel pour générer des rapports. Cela aurait nécessité l'intégration de bibliothèques supplémentaires comme Apache POI ou iText.

2. **Système de gestion des rôles avancé** : Actuellement, tous les utilisateurs authentifiés ont les mêmes droits. Il aurait été intéressant d'implémenter différents rôles (administrateur, tuteur simple) avec des permissions différentes.

3. **Upload de fichiers** : Permettre aux étudiants d'uploader leurs rapports directement dans l'application. Cette fonctionnalité a été mise de côté par manque de temps et pour éviter les problèmes de stockage en production.

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
- `spring-boot-starter-actuator` pour le monitoring et la supervision

### 5.2 Stack Technique

La stack utilisée est identique à celle du TP fil rouge :

- **Backend :** Spring Boot, Spring Data JPA, Spring Security
- **Frontend :** Thymeleaf, Bootstrap, JavaScript
- **Base de données :** PostgreSQL
- **Build :** Maven
- **Tests :** JUnit 5, Mockito
- **Monitoring :** Spring Boot Actuator
- **Autres :** Lombok, MapStruct, SpringDoc OpenAPI

### 5.3 Documentation API avec Swagger

La documentation API est disponible via SpringDoc OpenAPI (Swagger UI). La configuration se trouve dans `OpenApiConfig.java`. L'interface Swagger est accessible à l'adresse `/swagger-ui.html`.

Dans notre application, Swagger sert uniquement de documentation et non d'outil de test. En effet, nos contrôleurs sont annotés avec `@Controller` (et non `@RestController`) car ils retournent des vues Thymeleaf. Les endpoints documentés par Swagger sont donc principalement utilisés en arrière-plan par l'interface web, et ne peuvent pas être testés directement via l'interface Swagger UI de manière fonctionnelle (les retours sont des redirections vers des pages HTML plutôt que du JSON pur).

### 5.4 Déploiement

L'application est déployée sur Render.com et accessible à l'adresse : [https://altn72-projet-java-app.onrender.com](https://altn72-projet-java-app.onrender.com)

### 5.5 Gestion des Exceptions

Nous avons implémenté une gestion globale des exceptions avec **@ControllerAdvice** (`GlobalExceptionHandler`) qui gère :

- **ResourceNotFoundException** : Ressources introuvables (404)
- **DatabaseException** : Erreurs de base de données (500)
- **DataIntegrityViolationException** : Violations de contraintes d'intégrité

Le gestionnaire distingue les requêtes de gestion (qui sont redirigées avec un message flash) des autres requêtes (qui lèvent une `ResponseStatusException`). Les messages d'erreur sont personnalisés en français selon le contexte (email dupliqué, suppression impossible, etc.).

### 5.6 Requêtes JPQL et SQL Natives - Analyse Critique

#### Utilisation de JPQL

Dans `SchoolYearRepository`, nous privilégions JPQL pour la portabilité :

```java
@Query("SELECT DISTINCT sy.academicYear FROM SchoolYear sy ORDER BY sy.academicYear DESC")
List<String> findDistinctAcademicYearByOrderByAcademicYearDesc();
```

#### Utilisation de SQL Native

Pour les requêtes complexes de recherche multi-critères dans `StudentRepository`, nous utilisons du SQL natif :

```java
@Query(value = """
    SELECT DISTINCT s.*
    FROM student s
    LEFT JOIN school_year sy ON s.id = sy.student_id
    LEFT JOIN company c ON c.id = sy.company_id
    WHERE (s.is_archived IS NULL OR s.is_archived = false)
      AND (:name IS NULL OR LOWER(s.last_name) LIKE LOWER(CONCAT('%', :name, '%')))
      AND (:company IS NULL OR (c.id IS NOT NULL AND LOWER(c.company_name) LIKE LOWER(CONCAT('%', :company, '%'))))
      AND (:missionKeyword IS NULL OR LOWER(COALESCE(sy.keywords, '')) LIKE LOWER(CONCAT('%', :missionKeyword, '%')))
      AND (:academicYear IS NULL OR (sy.id IS NOT NULL AND sy.academic_year = :academicYear))
""", nativeQuery = true)
List<Student> searchStudents(...);
```

**Analyse critique :**

**Avantages du SQL natif :**

- Performance optimale pour les requêtes complexes avec plusieurs LEFT JOIN
- Permet l'utilisation de fonctions SQL spécifiques (COALESCE, CONCAT)
- Plus lisible pour les requêtes de recherche complexes

**Inconvénients :**

- Perte de la portabilité entre SGBD
- Pas de vérification de type à la compilation
- Nécessite une attention particulière lors des changements de schéma

**Conclusion :**
Nous utilisons JPQL par défaut pour sa portabilité et sa maintenabilité. Le SQL natif est réservé aux cas spéciaux, par exemple des entreprise qui ont des requete bien définie qui ne peuvent pas se permettre de tout changer en jpql.

### 5.7 Utilisation de @Transactional

Nous avons utilisé `@Transactional` dans les services, principalement pour :

**Justification :**

- **Cohérence des données** : En cas d'erreur, toutes les modifications sont annulées (rollback)
- **Performance** : Une seule connexion à la base de données pour plusieurs requêtes
- **Lazy Loading** : Permet le chargement des relations à la demande dans les méthodes de service

- Nous n'avons pas annoté les méthodes de lecture simple avec `@Transactional` parce que Spring le fait déjà implicitement.

La méthode `searchStudents()` utilise `@Transactional(readOnly = true)` malgré qu'elle soit une lecture. Ceci est nécessaire car notre requête SQL native ne charge pas automatiquement les collections lazy (`schoolYears`). La transaction garde la session Hibernate ouverte, permettant le chargement explicite de ces collections avant la conversion en DTO.

### 5.8 Initialisation des Données

Nous avons implémenté un système d'initialisation automatique des données (`DataInitializer`) qui :

- Crée un utilisateur de test au premier démarrage
- Génère des fixtures réalistes (12 étudiants, 5 entreprises, 11 mentors)
- Initialise des années scolaires avec missions, rapports et visites
- Permet de tester l'application immédiatement après le premier lancement

Ce système utilise un `CommandLineRunner` qui s'exécute uniquement si la base de données est vide, évitant ainsi la duplication des données lors des redémarrages.

## 6. Architecture et Qualité du Code

### 6.1 Séparation des Responsabilités

Le projet respecte une architecture en couches stricte :

- **Contrôleurs** (`controler/`) : Gestion des requêtes HTTP
- **Services** (`service/` et `service/impl/`) : Logique métier
- **Repositories** (`model/entities/repository/`) : Accès aux données
- **DTOs** (`model/dto/`) : Objets de transfert de données
- **Mappers** (`model/mapper/`) : Conversion entre entités et DTOs
- **Exceptions** (`exception/`) : Gestion centralisée des erreurs

### 6.2 Tests Unitaires

L'application dispose de tests unitaires pour tous les services :

- `CompanyServiceImplTest`
- `DashboardServiceImplTest`
- `MentorServiceImplTest`
- `SchoolYearServiceImplTest`
- `StudentProfileServiceImplTest`
- `StudentServiceImplTest`
- `VisitServiceImplTest`

Les tests utilisent Mockito pour mocker les dépendances et JUnit 5 pour l'exécution.

## 7. Conclusion

Ce projet nous a permis de mettre en pratique les concepts avancés de Spring Boot dans un contexte réel de gestion d'alternants. Nous avons réussi à développer une application complète, testée et déployée en production, en respectant les bonnes pratiques de développement et les principes SOLID.
