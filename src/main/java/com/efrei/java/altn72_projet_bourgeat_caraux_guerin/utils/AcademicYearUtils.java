package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.utils;

import java.time.LocalDate;
import java.util.List;

/**
 * Classe utilitaire pour la gestion des années académiques
 */
public class AcademicYearUtils {

    private AcademicYearUtils() {}

    /**
     * Obtient l'année académique actuelle
     * L'année académique commence en septembre
     * @return L'année académique actuelle
     */
    public static String getCurrentAcademicYear() {
        LocalDate now = LocalDate.now();
        int currentYear = now.getYear();
        int currentMonth = now.getMonthValue();
        
        if (currentMonth >= 9) {
            return String.format("%d/%d", currentYear, currentYear + 1);
        } else {
            return String.format("%d/%d", currentYear - 1, currentYear);
        }
    }

    /**
     * Obtient l'année académique la plus proche de l'année actuelle
     * @param availableYears Liste des années académiques disponibles
     * @return L'année académique la plus proche ou une chaîne vide si la liste est vide
     */
    public static String findClosestAcademicYear(List<String> availableYears) {
        if (availableYears == null || availableYears.isEmpty()) {
            return "";
        }

        String currentAcademicYear = getCurrentAcademicYear();

        if (availableYears.contains(currentAcademicYear)) {
            return currentAcademicYear;
        }

        int currentYear = LocalDate.now().getYear();
        return availableYears.stream()
            .min((y1, y2) -> {
                int year1 = Integer.parseInt(y1.split("/")[0]);
                int year2 = Integer.parseInt(y2.split("/")[0]);
                return Math.abs(year1 - currentYear) - Math.abs(year2 - currentYear);
            })
            .orElse(availableYears.get(0));
    }

    /**
     * Calcule l'année académique suivante
     * @param academicYear L'année académique actuelle
     * @return L'année académique suivante
     */
    public static String getNextAcademicYear(String academicYear) {
        String[] years = academicYear.split("/");
        int startYear = Integer.parseInt(years[0]);
        return (startYear + 1) + "/" + (startYear + 2);
    }

    /**
     * Convertit une année académique au format URL
     * @param academicYear L'année académique
     * @return L'année académique pour URL
     */
    public static String toUrlFormat(String academicYear) {
        return academicYear.replace("/", "-");
    }

    /**
     * Convertit une année académique depuis le format URL
     * @param academicYear L'année académique
     * @return L'année académique standard
     */ 
    public static String fromUrlFormat(String academicYear) {
        return academicYear.replace("-", "/");
    }
}
