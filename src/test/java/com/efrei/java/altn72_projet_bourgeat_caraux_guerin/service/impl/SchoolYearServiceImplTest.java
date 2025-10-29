package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.service.impl;

import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.repository.SchoolYearRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour SchoolYearServiceImpl
 */
@ExtendWith(MockitoExtension.class)
class SchoolYearServiceImplTest {

    @Mock
    private SchoolYearRepository schoolYearRepository;

    @InjectMocks
    private SchoolYearServiceImpl schoolYearService;

    @Test
    void testGetAllAcademicYears() {
        // Arrange
        List<String> academicYears = Arrays.asList("2025/2026", "2024/2025", "2023/2024");
        when(schoolYearRepository.findDistinctAcademicYearByOrderByAcademicYearDesc()).thenReturn(academicYears);

        // Act
        List<String> result = schoolYearService.getAllAcademicYears();

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("2025/2026", result.get(0));
        assertEquals("2024/2025", result.get(1));
        assertEquals("2023/2024", result.get(2));
        verify(schoolYearRepository, times(1)).findDistinctAcademicYearByOrderByAcademicYearDesc();
    }
}

