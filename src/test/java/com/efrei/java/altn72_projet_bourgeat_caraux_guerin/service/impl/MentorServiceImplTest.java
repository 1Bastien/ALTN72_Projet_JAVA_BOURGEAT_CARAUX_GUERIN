package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.service.impl;

import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto.MentorDTO;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.Company;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.Mentor;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.repository.CompanyRepository;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.repository.MentorRepository;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.mapper.MentorMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour MentorServiceImpl
 */
@ExtendWith(MockitoExtension.class)
class MentorServiceImplTest {

    @Mock
    private MentorRepository mentorRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private MentorMapper mentorMapper;

    @InjectMocks
    private MentorServiceImpl mentorService;

    @Test
    void testGetAllMentors() {
        // Arrange
        Mentor mentor1 = new Mentor();
        mentor1.setId(1L);
        mentor1.setLastName("Dupont");
        mentor1.setFirstName("Jean");

        Mentor mentor2 = new Mentor();
        mentor2.setId(2L);
        mentor2.setLastName("Martin");
        mentor2.setFirstName("Marie");

        List<Mentor> mentors = Arrays.asList(mentor1, mentor2);

        MentorDTO dto1 = new MentorDTO();
        dto1.setId(1L);
        dto1.setLastName("Dupont");

        MentorDTO dto2 = new MentorDTO();
        dto2.setId(2L);
        dto2.setLastName("Martin");

        List<MentorDTO> expectedDTOs = Arrays.asList(dto1, dto2);

        when(mentorRepository.findAll()).thenReturn(mentors);
        when(mentorMapper.toDTOList(mentors)).thenReturn(expectedDTOs);

        // Act
        List<MentorDTO> result = mentorService.getAllMentors();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(mentorRepository, times(1)).findAll();
        verify(mentorMapper, times(1)).toDTOList(mentors);
    }

    @Test
    void testGetMentorsByCompanyId() {
        // Arrange
        Long companyId = 1L;
        Mentor mentor = new Mentor();
        mentor.setId(1L);
        mentor.setLastName("Dupont");

        List<Mentor> mentors = Arrays.asList(mentor);

        MentorDTO dto = new MentorDTO();
        dto.setId(1L);
        dto.setLastName("Dupont");

        List<MentorDTO> expectedDTOs = Arrays.asList(dto);

        when(mentorRepository.findByCompanyId(companyId)).thenReturn(mentors);
        when(mentorMapper.toDTOList(mentors)).thenReturn(expectedDTOs);

        // Act
        List<MentorDTO> result = mentorService.getMentorsByCompanyId(companyId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(mentorRepository, times(1)).findByCompanyId(companyId);
        verify(mentorMapper, times(1)).toDTOList(mentors);
    }

    @Test
    void testGetMentorById() {
        // Arrange
        Long mentorId = 1L;
        Mentor mentor = new Mentor();
        mentor.setId(mentorId);
        mentor.setLastName("Dupont");
        mentor.setFirstName("Jean");

        MentorDTO expectedDTO = new MentorDTO();
        expectedDTO.setId(mentorId);
        expectedDTO.setLastName("Dupont");
        expectedDTO.setFirstName("Jean");

        when(mentorRepository.findById(mentorId)).thenReturn(Optional.of(mentor));
        when(mentorMapper.toDTO(mentor)).thenReturn(expectedDTO);

        // Act
        MentorDTO result = mentorService.getMentorById(mentorId);

        // Assert
        assertNotNull(result);
        assertEquals(mentorId, result.getId());
        assertEquals("Dupont", result.getLastName());
        verify(mentorRepository, times(1)).findById(mentorId);
        verify(mentorMapper, times(1)).toDTO(mentor);
    }

    @Test
    void testCreateMentor() {
        // Arrange
        Long companyId = 1L;
        MentorDTO inputDTO = new MentorDTO();
        inputDTO.setLastName("Dupont");
        inputDTO.setFirstName("Jean");
        inputDTO.setEmail("jean.dupont@example.com");
        inputDTO.setCompanyId(companyId);

        Company company = new Company();
        company.setId(companyId);
        company.setCompanyName("Entreprise Test");

        Mentor mentor = new Mentor();
        mentor.setLastName("Dupont");
        mentor.setFirstName("Jean");
        mentor.setEmail("jean.dupont@example.com");

        Mentor savedMentor = new Mentor();
        savedMentor.setId(1L);
        savedMentor.setLastName("Dupont");
        savedMentor.setFirstName("Jean");
        savedMentor.setEmail("jean.dupont@example.com");
        savedMentor.setCompany(company);

        MentorDTO expectedDTO = new MentorDTO();
        expectedDTO.setId(1L);
        expectedDTO.setLastName("Dupont");
        expectedDTO.setFirstName("Jean");
        expectedDTO.setEmail("jean.dupont@example.com");
        expectedDTO.setCompanyId(companyId);

        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        when(mentorMapper.toEntity(inputDTO)).thenReturn(mentor);
        when(mentorRepository.save(any(Mentor.class))).thenReturn(savedMentor);
        when(mentorMapper.toDTO(savedMentor)).thenReturn(expectedDTO);

        // Act
        MentorDTO result = mentorService.createMentor(inputDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Dupont", result.getLastName());
        assertEquals("Jean", result.getFirstName());
        verify(companyRepository, times(1)).findById(companyId);
        verify(mentorMapper, times(1)).toEntity(inputDTO);
        verify(mentorRepository, times(1)).save(any(Mentor.class));
        verify(mentorMapper, times(1)).toDTO(savedMentor);
    }

    @Test
    void testUpdateMentor() {
        // Arrange
        Long mentorId = 1L;
        Long companyId = 2L;
        MentorDTO updateDTO = new MentorDTO();
        updateDTO.setLastName("Dupont Modifié");
        updateDTO.setFirstName("Jean");
        updateDTO.setEmail("jean.dupont@example.com");
        updateDTO.setCompanyId(companyId);

        Company company = new Company();
        company.setId(companyId);
        company.setCompanyName("Nouvelle Entreprise");

        Mentor mentor = new Mentor();
        mentor.setLastName("Dupont Modifié");
        mentor.setFirstName("Jean");
        mentor.setEmail("jean.dupont@example.com");

        Mentor updatedMentor = new Mentor();
        updatedMentor.setId(mentorId);
        updatedMentor.setLastName("Dupont Modifié");
        updatedMentor.setFirstName("Jean");
        updatedMentor.setEmail("jean.dupont@example.com");
        updatedMentor.setCompany(company);

        MentorDTO expectedDTO = new MentorDTO();
        expectedDTO.setId(mentorId);
        expectedDTO.setLastName("Dupont Modifié");
        expectedDTO.setFirstName("Jean");
        expectedDTO.setEmail("jean.dupont@example.com");
        expectedDTO.setCompanyId(companyId);

        when(mentorRepository.existsById(mentorId)).thenReturn(true);
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        when(mentorMapper.toEntity(updateDTO)).thenReturn(mentor);
        when(mentorRepository.save(any(Mentor.class))).thenReturn(updatedMentor);
        when(mentorMapper.toDTO(updatedMentor)).thenReturn(expectedDTO);

        // Act
        MentorDTO result = mentorService.updateMentor(mentorId, updateDTO);

        // Assert
        assertNotNull(result);
        assertEquals(mentorId, result.getId());
        assertEquals("Dupont Modifié", result.getLastName());
        verify(mentorRepository, times(1)).existsById(mentorId);
        verify(companyRepository, times(1)).findById(companyId);
        verify(mentorMapper, times(1)).toEntity(updateDTO);
        verify(mentorRepository, times(1)).save(any(Mentor.class));
        verify(mentorMapper, times(1)).toDTO(updatedMentor);
    }

    @Test
    void testDeleteMentor() {
        // Arrange
        Long mentorId = 1L;
        Mentor mentor = new Mentor();
        mentor.setId(mentorId);
        mentor.setLastName("Dupont");

        when(mentorRepository.findById(mentorId)).thenReturn(Optional.of(mentor));
        doNothing().when(mentorRepository).delete(mentor);

        // Act
        mentorService.deleteMentor(mentorId);

        // Assert
        verify(mentorRepository, times(1)).findById(mentorId);
        verify(mentorRepository, times(1)).delete(mentor);
    }
}

