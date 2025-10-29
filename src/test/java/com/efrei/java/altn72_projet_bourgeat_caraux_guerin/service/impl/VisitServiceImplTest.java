package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.service.impl;

import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto.VisitDTO;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.SchoolYear;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.Visit;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.repository.SchoolYearRepository;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.repository.VisitRepository;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.enums.Format;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.mapper.VisitMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour VisitServiceImpl
 */
@ExtendWith(MockitoExtension.class)
class VisitServiceImplTest {

    @Mock
    private VisitRepository visitRepository;

    @Mock
    private SchoolYearRepository schoolYearRepository;

    @Mock
    private VisitMapper visitMapper;

    @InjectMocks
    private VisitServiceImpl visitService;

    @Test
    void testGetVisitsBySchoolYearId() {
        // Arrange
        Long schoolYearId = 1L;
        Visit visit1 = new Visit();
        visit1.setId(1L);
        visit1.setDate(LocalDateTime.now());
        visit1.setFormat(Format.IN_PERSON);

        Visit visit2 = new Visit();
        visit2.setId(2L);
        visit2.setDate(LocalDateTime.now());
        visit2.setFormat(Format.ONLINE);

        List<Visit> visits = Arrays.asList(visit1, visit2);

        VisitDTO dto1 = new VisitDTO();
        dto1.setId(1L);
        dto1.setFormat(Format.IN_PERSON);

        VisitDTO dto2 = new VisitDTO();
        dto2.setId(2L);
        dto2.setFormat(Format.ONLINE);

        List<VisitDTO> expectedDTOs = Arrays.asList(dto1, dto2);

        when(visitRepository.findBySchoolYearId(schoolYearId)).thenReturn(visits);
        when(visitMapper.toDTOList(visits)).thenReturn(expectedDTOs);

        // Act
        List<VisitDTO> result = visitService.getVisitsBySchoolYearId(schoolYearId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(visitRepository, times(1)).findBySchoolYearId(schoolYearId);
        verify(visitMapper, times(1)).toDTOList(visits);
    }

    @Test
    void testGetVisitById() {
        // Arrange
        Long visitId = 1L;
        Visit visit = new Visit();
        visit.setId(visitId);
        visit.setDate(LocalDateTime.now());
        visit.setFormat(Format.IN_PERSON);
        visit.setComment("Visite test");

        VisitDTO expectedDTO = new VisitDTO();
        expectedDTO.setId(visitId);
        expectedDTO.setFormat(Format.IN_PERSON);
        expectedDTO.setComment("Visite test");

        when(visitRepository.findById(visitId)).thenReturn(Optional.of(visit));
        when(visitMapper.toDTO(visit)).thenReturn(expectedDTO);

        // Act
        VisitDTO result = visitService.getVisitById(visitId);

        // Assert
        assertNotNull(result);
        assertEquals(visitId, result.getId());
        assertEquals(Format.IN_PERSON, result.getFormat());
        verify(visitRepository, times(1)).findById(visitId);
        verify(visitMapper, times(1)).toDTO(visit);
    }

    @Test
    void testCreateVisit() {
        // Arrange
        Long schoolYearId = 1L;
        VisitDTO inputDTO = new VisitDTO();
        inputDTO.setDate(LocalDateTime.now());
        inputDTO.setFormat(Format.IN_PERSON);
        inputDTO.setComment("Nouvelle visite");
        inputDTO.setSchoolYearId(schoolYearId);

        SchoolYear schoolYear = new SchoolYear();
        schoolYear.setId(schoolYearId);
        schoolYear.setAcademicYear("2024/2025");

        Visit visit = new Visit();
        visit.setDate(inputDTO.getDate());
        visit.setFormat(inputDTO.getFormat());
        visit.setComment(inputDTO.getComment());

        Visit savedVisit = new Visit();
        savedVisit.setId(1L);
        savedVisit.setDate(inputDTO.getDate());
        savedVisit.setFormat(inputDTO.getFormat());
        savedVisit.setComment(inputDTO.getComment());
        savedVisit.setSchoolYear(schoolYear);

        VisitDTO expectedDTO = new VisitDTO();
        expectedDTO.setId(1L);
        expectedDTO.setFormat(Format.IN_PERSON);
        expectedDTO.setComment("Nouvelle visite");
        expectedDTO.setSchoolYearId(schoolYearId);

        when(schoolYearRepository.findById(schoolYearId)).thenReturn(Optional.of(schoolYear));
        when(visitMapper.toEntity(inputDTO)).thenReturn(visit);
        when(visitRepository.save(any(Visit.class))).thenReturn(savedVisit);
        when(visitMapper.toDTO(savedVisit)).thenReturn(expectedDTO);

        // Act
        VisitDTO result = visitService.createVisit(inputDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(Format.IN_PERSON, result.getFormat());
        verify(schoolYearRepository, times(1)).findById(schoolYearId);
        verify(visitMapper, times(1)).toEntity(inputDTO);
        verify(visitRepository, times(1)).save(any(Visit.class));
        verify(visitMapper, times(1)).toDTO(savedVisit);
    }

    @Test
    void testUpdateVisit() {
        // Arrange
        Long visitId = 1L;
        Long schoolYearId = 2L;

        VisitDTO updateDTO = new VisitDTO();
        updateDTO.setDate(LocalDateTime.now());
        updateDTO.setFormat(Format.ONLINE);
        updateDTO.setComment("Visite modifiée");
        updateDTO.setSchoolYearId(schoolYearId);

        Visit existingVisit = new Visit();
        existingVisit.setId(visitId);
        existingVisit.setDate(LocalDateTime.now().minusDays(1));
        existingVisit.setFormat(Format.IN_PERSON);

        SchoolYear schoolYear = new SchoolYear();
        schoolYear.setId(schoolYearId);

        Visit updatedVisit = new Visit();
        updatedVisit.setId(visitId);
        updatedVisit.setDate(updateDTO.getDate());
        updatedVisit.setFormat(updateDTO.getFormat());
        updatedVisit.setComment(updateDTO.getComment());
        updatedVisit.setSchoolYear(schoolYear);

        VisitDTO expectedDTO = new VisitDTO();
        expectedDTO.setId(visitId);
        expectedDTO.setFormat(Format.ONLINE);
        expectedDTO.setComment("Visite modifiée");
        expectedDTO.setSchoolYearId(schoolYearId);

        when(visitRepository.findById(visitId)).thenReturn(Optional.of(existingVisit));
        when(schoolYearRepository.findById(schoolYearId)).thenReturn(Optional.of(schoolYear));
        when(visitRepository.save(any(Visit.class))).thenReturn(updatedVisit);
        when(visitMapper.toDTO(updatedVisit)).thenReturn(expectedDTO);

        // Act
        VisitDTO result = visitService.updateVisit(visitId, updateDTO);

        // Assert
        assertNotNull(result);
        assertEquals(visitId, result.getId());
        assertEquals(Format.ONLINE, result.getFormat());
        assertEquals("Visite modifiée", result.getComment());
        verify(visitRepository, times(1)).findById(visitId);
        verify(schoolYearRepository, times(1)).findById(schoolYearId);
        verify(visitRepository, times(1)).save(any(Visit.class));
        verify(visitMapper, times(1)).toDTO(updatedVisit);
    }

    @Test
    void testDeleteVisit() {
        // Arrange
        Long visitId = 1L;
        Visit visit = new Visit();
        visit.setId(visitId);
        visit.setDate(LocalDateTime.now());
        visit.setFormat(Format.IN_PERSON);

        when(visitRepository.findById(visitId)).thenReturn(Optional.of(visit));
        doNothing().when(visitRepository).delete(visit);

        // Act
        visitService.deleteVisit(visitId);

        // Assert
        verify(visitRepository, times(1)).findById(visitId);
        verify(visitRepository, times(1)).delete(visit);
    }
}

