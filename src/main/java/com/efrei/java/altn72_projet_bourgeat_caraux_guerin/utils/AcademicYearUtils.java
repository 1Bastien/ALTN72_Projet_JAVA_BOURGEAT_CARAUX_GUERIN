package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.utils;

import java.time.LocalDate;
import java.util.List;

/**
 * Classe utilitaire pour la gestion des années académiques
 */
public class AcademicYearUtils {

    private AcademicYearUtils() {}

    /**
     * Obtient l'année académique actuelle (ex: "2025/2026" si on est en 2025)
     * @return L'année académique actuelle
     */
    public static String getCurrentAcademicYear() {
        int currentYear = LocalDate.now().getYear();
        return String.format("%d/%d", currentYear, currentYear + 1);
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
}
