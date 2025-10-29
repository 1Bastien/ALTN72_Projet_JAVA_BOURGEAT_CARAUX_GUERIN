package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.config;

import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.*;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.repository.*;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.enums.Format;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.enums.Program;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Initialisation des données au démarrage de l'application
 */
@Configuration
public class DataInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    private final Random random = new Random();

    /**
     * Crée un utilisateur tuteur par défaut et des fixtures si la base de données est vide
     * @param userRepository Le repository des utilisateurs
     * @param passwordEncoder L'encodeur de mot de passe
     * @param companyRepository Le repository des entreprises
     * @param mentorRepository Le repository des mentors
     * @param studentRepository Le repository des étudiants
     * @param schoolYearRepository Le repository des années scolaires
     * @param visitRepository Le repository des visites
     * @return Le CommandLineRunner qui initialise les données
     */
    @Bean
    public CommandLineRunner initData(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            CompanyRepository companyRepository,
            MentorRepository mentorRepository,
            StudentRepository studentRepository,
            SchoolYearRepository schoolYearRepository,
            VisitRepository visitRepository) {
        return args -> {
            if (userRepository.count() == 0) {
                logger.info("Initialisation des données de l'application...");
                
                User tuteur = createTuteur(userRepository, passwordEncoder);
                
                List<Company> companies = createCompanies(companyRepository);
                List<Mentor> mentors = createMentors(mentorRepository, companies);
                
                List<Student> students = createStudents(studentRepository, schoolYearRepository, companies, mentors, visitRepository);
                
                logger.info("Données initialisées avec succès !");
                logger.info("   Utilisateur tuteur : {} / {}", tuteur.getUsername(), "tuteur");
                logger.info("   {} entreprises créées", companies.size());
                logger.info("   {} mentors créés", mentors.size());
                logger.info("   {} étudiants créés avec succès (dont {} archivés)", students.size(), 
                    students.stream().filter(s -> Boolean.TRUE.equals(s.getIsArchived())).count());
            } else {
                logger.info("Des utilisateurs existent déjà dans la base de données. Aucune initialisation nécessaire.");
            }
        };
    }

    private User createTuteur(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        User tuteur = new User();
        tuteur.setFirstName("Jean");
        tuteur.setLastName("Dupont");
        tuteur.setUsername("jean.dupont");
        tuteur.setPassword(passwordEncoder.encode("motdepasse"));
        tuteur.setEmail("jean.dupont@efrei.fr");
        userRepository.save(tuteur);
        return tuteur;
    }

    private List<Company> createCompanies(CompanyRepository companyRepository) {
        List<Company> companies = new ArrayList<>();
        
        Company company1 = new Company();
        company1.setCompanyName("Capgemini");
        company1.setAddress("11 rue de Tilsitt, 75017 Paris");
        company1.setAccessInformation("Entrée principale, badge visiteur à l'accueil");
        companies.add(companyRepository.save(company1));
        
        Company company2 = new Company();
        company2.setCompanyName("Société Générale");
        company2.setAddress("29 boulevard Haussmann, 75009 Paris");
        company2.setAccessInformation("Tours Société Générale, Tour Granite");
        companies.add(companyRepository.save(company2));
        
        Company company3 = new Company();
        company3.setCompanyName("Atos");
        company3.setAddress("River Ouest, 80 quai Voltaire, 95870 Bezons");
        company3.setAccessInformation("Campus Atos, parking visiteurs disponible");
        companies.add(companyRepository.save(company3));
        
        Company company4 = new Company();
        company4.setCompanyName("Thales");
        company4.setAddress("Tour Carpe Diem, 31 place des Corolles, 92400 Courbevoie");
        company4.setAccessInformation("Badge obligatoire, accueil au RDC");
        companies.add(companyRepository.save(company4));
        
        Company company5 = new Company();
        company5.setCompanyName("BNP Paribas");
        company5.setAddress("16 boulevard des Italiens, 75009 Paris");
        company5.setAccessInformation("Siège social, entrée par le hall principal");
        companies.add(companyRepository.save(company5));
        
        logger.info("    {} entreprises créées avec succès", companies.size());
        return companies;
    }

    private List<Mentor> createMentors(MentorRepository mentorRepository, List<Company> companies) {
        List<Mentor> mentors = new ArrayList<>();
        
        // Capgemini - 3 mentors
        mentors.add(createMentor(mentorRepository, "Martin", "Sophie", "sophie.martin@capgemini.com", 
            "0145678901", "Architecte Solutions", companies.get(0)));
        mentors.add(createMentor(mentorRepository, "Bernard", "Thomas", "thomas.bernard@capgemini.com", 
            "0145678902", "Lead Developer", companies.get(0)));
        mentors.add(createMentor(mentorRepository, "Petit", "Julie", "julie.petit@capgemini.com", 
            "0145678903", "Chef de Projet", companies.get(0)));
        
        // Société Générale - 2 mentors
        mentors.add(createMentor(mentorRepository, "Robert", "Pierre", "pierre.robert@socgen.com", 
            "0142678901", "Responsable Sécurité", companies.get(1)));
        mentors.add(createMentor(mentorRepository, "Durand", "Claire", "claire.durand@socgen.com", 
            "0142678902", "Data Engineer", companies.get(1)));
        
        // Atos - 3 mentors
        mentors.add(createMentor(mentorRepository, "Moreau", "Laurent", "laurent.moreau@atos.net", 
            "0143678901", "DevOps Lead", companies.get(2)));
        mentors.add(createMentor(mentorRepository, "Simon", "Isabelle", "isabelle.simon@atos.net", 
            "0143678902", "Scrum Master", companies.get(2)));
        mentors.add(createMentor(mentorRepository, "Laurent", "Nicolas", "nicolas.laurent@atos.net", 
            "0143678903", "Tech Lead", companies.get(2)));
        
        // Thales - 1 mentor
        mentors.add(createMentor(mentorRepository, "Michel", "François", "francois.michel@thalesgroup.com", 
            "0144678901", "Ingénieur Systèmes", companies.get(3)));
        
        // BNP Paribas - 2 mentors
        mentors.add(createMentor(mentorRepository, "Lefebvre", "Céline", "celine.lefebvre@bnpparibas.com", 
            "0146678901", "Product Owner", companies.get(4)));
        mentors.add(createMentor(mentorRepository, "Roux", "Alexandre", "alexandre.roux@bnpparibas.com", 
            "0146678902", "Full Stack Developer", companies.get(4)));
        
        logger.info("   ✓ {} mentors créés", mentors.size());
        return mentors;
    }

    private Mentor createMentor(MentorRepository mentorRepository, String lastName, String firstName, 
                                String email, String phone, String jobTitle, Company company) {
        Mentor mentor = new Mentor();
        mentor.setLastName(lastName);
        mentor.setFirstName(firstName);
        mentor.setEmail(email);
        mentor.setPhone(phone);
        mentor.setJobTitle(jobTitle);
        mentor.setCompany(company);
        return mentorRepository.save(mentor);
    }

    private List<Student> createStudents(StudentRepository studentRepository, 
                                        SchoolYearRepository schoolYearRepository,
                                        List<Company> companies, 
                                        List<Mentor> mentors,
                                        VisitRepository visitRepository) {
        List<Student> students = new ArrayList<>();
        
        // Étudiants archivés (2 étudiants ayant terminé leur M2)
        students.add(createArchivedStudent(studentRepository, schoolYearRepository, 
            "Dubois", "Jean", "jean.dubois@efrei.net", "0601020304", 
            companies, mentors, visitRepository));
        students.add(createArchivedStudent(studentRepository, schoolYearRepository, 
            "Leroy", "Camille", "camille.leroy@efrei.net", "0602030405", 
            companies, mentors, visitRepository));
        
        // Étudiants actifs avec parcours variés
        students.add(createActiveStudent(studentRepository, schoolYearRepository, 
            "Garnier", "Lucas", "lucas.garnier@efrei.net", "0603040506", 
            5, companies, mentors, visitRepository)); // 5 années
        
        students.add(createActiveStudent(studentRepository, schoolYearRepository, 
            "Rousseau", "Emma", "emma.rousseau@efrei.net", "0604050607", 
            4, companies, mentors, visitRepository)); // 4 années
        
        students.add(createActiveStudent(studentRepository, schoolYearRepository, 
            "Bonnet", "Hugo", "hugo.bonnet@efrei.net", "0605060708", 
            3, companies, mentors, visitRepository)); // 3 années
        
        students.add(createActiveStudent(studentRepository, schoolYearRepository, 
            "Lambert", "Léa", "lea.lambert@efrei.net", "0606070809", 
            3, companies, mentors, visitRepository)); // 3 années
        
        students.add(createActiveStudent(studentRepository, schoolYearRepository, 
            "Fontaine", "Nathan", "nathan.fontaine@efrei.net", "0607080910", 
            2, companies, mentors, visitRepository)); // 2 années
        
        students.add(createActiveStudent(studentRepository, schoolYearRepository, 
            "Chevalier", "Chloé", "chloe.chevalier@efrei.net", "0608091011", 
            2, companies, mentors, visitRepository)); // 2 années
        
        students.add(createActiveStudent(studentRepository, schoolYearRepository, 
            "Girard", "Tom", "tom.girard@efrei.net", "0609101112", 
            2, companies, mentors, visitRepository)); // 2 années
        
        students.add(createActiveStudent(studentRepository, schoolYearRepository, 
            "Faure", "Sarah", "sarah.faure@efrei.net", "0610111213", 
            1, companies, mentors, visitRepository)); // 1 année
        
        students.add(createActiveStudent(studentRepository, schoolYearRepository, 
            "Andre", "Louis", "louis.andre@efrei.net", "0611121314", 
            1, companies, mentors, visitRepository)); // 1 année
        
        students.add(createActiveStudent(studentRepository, schoolYearRepository, 
            "Mercier", "Zoé", "zoe.mercier@efrei.net", "0612131415", 
            1, companies, mentors, visitRepository)); // 1 année
        
        logger.info("   ✓ {} étudiants créés", students.size());
        return students;
    }

    private Student createArchivedStudent(StudentRepository studentRepository, 
                                         SchoolYearRepository schoolYearRepository,
                                         String lastName, String firstName, 
                                         String email, String phone,
                                         List<Company> companies, 
                                         List<Mentor> mentors,
                                         VisitRepository visitRepository) {
        Student student = new Student();
        student.setLastName(lastName);
        student.setFirstName(firstName);
        student.setEmail(email);
        student.setPhone(phone);
        student.setIsArchived(true);
        student = studentRepository.save(student);
        
        Program[] programs = {Program.L1_APP, Program.L2_APP, Program.L3_APP, Program.M1_APP, Program.M2_APP};
        String[] years = {"2019/2020", "2020/2021", "2021/2022", "2022/2023", "2023/2024"};
        
        for (int i = 0; i < 5; i++) {
            Company company = companies.get(random.nextInt(companies.size()));
            Mentor mentor = mentors.stream()
                .filter(m -> m.getCompany().getId().equals(company.getId()))
                .findFirst()
                .orElse(mentors.get(0));
            
            SchoolYear schoolYear = createSchoolYear(student, programs[i], years[i], 
                company, mentor, true);
            schoolYearRepository.save(schoolYear);
            
            if (i >= 3) {
                createVisits(schoolYear, visitRepository, 2);
            }
        }
        
        return student;
    }

    private Student createActiveStudent(StudentRepository studentRepository, 
                                       SchoolYearRepository schoolYearRepository,
                                       String lastName, String firstName, 
                                       String email, String phone, int numYears,
                                       List<Company> companies, 
                                       List<Mentor> mentors,
                                       VisitRepository visitRepository) {
        Student student = new Student();
        student.setLastName(lastName);
        student.setFirstName(firstName);
        student.setEmail(email);
        student.setPhone(phone);
        student.setIsArchived(false);
        student = studentRepository.save(student);
        
        Program[] programs = {Program.L1_APP, Program.L2_APP, Program.L3_APP, Program.M1_APP, Program.M2_APP};
        int currentYear = 2024;
        int startYear = currentYear - numYears;
        
        for (int i = 0; i < numYears; i++) {
            int yearIndex = i;
            String academicYear = (startYear + i) + "/" + (startYear + i + 1);
            
            Company company = companies.get(random.nextInt(companies.size()));
            Mentor mentor = mentors.stream()
                .filter(m -> m.getCompany().getId().equals(company.getId()))
                .findFirst()
                .orElse(mentors.get(0));
            
            SchoolYear schoolYear = createSchoolYear(student, programs[yearIndex], academicYear, 
                company, mentor, i < numYears - 1); // Completed except for the current year
            schoolYearRepository.save(schoolYear);
            
            if (yearIndex >= 3) {
                createVisits(schoolYear, visitRepository, random.nextInt(3) + 1);
            }
        }
        
        return student;
    }

    private SchoolYear createSchoolYear(Student student, Program program, String academicYear,
                                       Company company, Mentor mentor, boolean completed) {
        SchoolYear schoolYear = new SchoolYear();
        schoolYear.setStudent(student);
        schoolYear.setProgram(program);
        schoolYear.setAcademicYear(academicYear);
        schoolYear.setMajor(getRandomMajor());
        schoolYear.setCompany(company);
        schoolYear.setMentor(mentor);
        
        Mission mission = new Mission();
        mission.setKeywords(getRandomKeywords());
        mission.setTargetJob(getRandomTargetJob());
        mission.setComment("Mission en " + company.getCompanyName());
        schoolYear.setMission(mission);
        
        if (completed) {
            Report report = new Report();
            report.setSubject("Rapport de stage - " + academicYear);
            report.setReportGrade(BigDecimal.valueOf(12 + random.nextInt(7) + random.nextDouble()).setScale(2, RoundingMode.HALF_UP));
            report.setComment("Bon travail réalisé");
            schoolYear.setReport(report);
            
            Presentation presentation = new Presentation();
            presentation.setDate(LocalDateTime.of(2024, 6, 15 + random.nextInt(10), 14, 0));
            presentation.setPresentationGrade(BigDecimal.valueOf(11 + random.nextInt(8) + random.nextDouble()).setScale(2, RoundingMode.HALF_UP));
            presentation.setComment("Présentation claire et structurée");
            schoolYear.setPresentation(presentation);
            
            schoolYear.setFeedback("Année réussie avec succès");
        }
        
        return schoolYear;
    }

    private void createVisits(SchoolYear schoolYear, VisitRepository visitRepository, int count) {
        for (int i = 0; i < count; i++) {
            Visit visit = new Visit();
            visit.setSchoolYear(schoolYear);
            visit.setDate(LocalDateTime.now().minusMonths(random.nextInt(6)));
            visit.setFormat(random.nextBoolean() ? Format.ONLINE : Format.IN_PERSON);
            visit.setComment("Visite de suivi, étudiant progresse bien");
            visitRepository.save(visit);
        }
    }

    private String getRandomMajor() {
        String[] majors = {"Informatique", "Big Data & IA", "Cybersécurité", "Cloud Computing", "DevOps"};
        return majors[random.nextInt(majors.length)];
    }

    private String getRandomKeywords() {
        String[] keywords = {
            "Java, Spring Boot, Angular", 
            "Python, Django, React", 
            "Cloud, AWS, Terraform",
            "DevOps, Docker, Kubernetes",
            "Data Science, Machine Learning",
            "Cybersécurité, Pentest",
            "Full Stack, Vue.js, Node.js"
        };
        return keywords[random.nextInt(keywords.length)];
    }

    private String getRandomTargetJob() {
        String[] jobs = {
            "Développeur Full Stack", 
            "Ingénieur DevOps", 
            "Data Engineer",
            "Architecte Solutions",
            "Consultant Technique",
            "Chef de Projet IT",
            "Ingénieur Cloud"
        };
        return jobs[random.nextInt(jobs.length)];
    }
}

