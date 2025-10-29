package com.efrei.java.altn72_projet_bourgeat_caraux_guerin.service.impl;

import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.dto.CompanyDTO;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.Company;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.entities.repository.CompanyRepository;
import com.efrei.java.altn72_projet_bourgeat_caraux_guerin.model.mapper.CompanyMapper;
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
 * Tests unitaires pour CompanyServiceImpl
 */
@ExtendWith(MockitoExtension.class)
class CompanyServiceImplTest {

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private CompanyMapper companyMapper;

    @InjectMocks
    private CompanyServiceImpl companyService;

    @Test
    void testGetAllCompanies() {
        // Arrange
        Company company1 = new Company();
        company1.setId(1L);
        company1.setCompanyName("Entreprise A");
        company1.setAddress("Adresse A");

        Company company2 = new Company();
        company2.setId(2L);
        company2.setCompanyName("Entreprise B");
        company2.setAddress("Adresse B");

        List<Company> companies = Arrays.asList(company1, company2);

        CompanyDTO dto1 = new CompanyDTO();
        dto1.setId(1L);
        dto1.setCompanyName("Entreprise A");

        CompanyDTO dto2 = new CompanyDTO();
        dto2.setId(2L);
        dto2.setCompanyName("Entreprise B");

        List<CompanyDTO> expectedDTOs = Arrays.asList(dto1, dto2);

        when(companyRepository.findAll()).thenReturn(companies);
        when(companyMapper.toDTOList(companies)).thenReturn(expectedDTOs);

        // Act
        List<CompanyDTO> result = companyService.getAllCompanies();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Entreprise A", result.get(0).getCompanyName());
        assertEquals("Entreprise B", result.get(1).getCompanyName());
        verify(companyRepository, times(1)).findAll();
        verify(companyMapper, times(1)).toDTOList(companies);
    }

    @Test
    void testGetCompanyById() {
        // Arrange
        Long companyId = 1L;
        Company company = new Company();
        company.setId(companyId);
        company.setCompanyName("Entreprise Test");
        company.setAddress("Adresse Test");

        CompanyDTO expectedDTO = new CompanyDTO();
        expectedDTO.setId(companyId);
        expectedDTO.setCompanyName("Entreprise Test");
        expectedDTO.setAddress("Adresse Test");

        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        when(companyMapper.toDTO(company)).thenReturn(expectedDTO);

        // Act
        CompanyDTO result = companyService.getCompanyById(companyId);

        // Assert
        assertNotNull(result);
        assertEquals(companyId, result.getId());
        assertEquals("Entreprise Test", result.getCompanyName());
        assertEquals("Adresse Test", result.getAddress());
        verify(companyRepository, times(1)).findById(companyId);
        verify(companyMapper, times(1)).toDTO(company);
    }

    @Test
    void testCreateCompany() {
        // Arrange
        CompanyDTO inputDTO = new CompanyDTO();
        inputDTO.setCompanyName("Nouvelle Entreprise");
        inputDTO.setAddress("Nouvelle Adresse");
        inputDTO.setAccessInformation("Infos d'accès");

        Company company = new Company();
        company.setCompanyName("Nouvelle Entreprise");
        company.setAddress("Nouvelle Adresse");
        company.setAccessInformation("Infos d'accès");

        Company savedCompany = new Company();
        savedCompany.setId(1L);
        savedCompany.setCompanyName("Nouvelle Entreprise");
        savedCompany.setAddress("Nouvelle Adresse");
        savedCompany.setAccessInformation("Infos d'accès");

        CompanyDTO expectedDTO = new CompanyDTO();
        expectedDTO.setId(1L);
        expectedDTO.setCompanyName("Nouvelle Entreprise");
        expectedDTO.setAddress("Nouvelle Adresse");
        expectedDTO.setAccessInformation("Infos d'accès");

        when(companyMapper.toEntity(inputDTO)).thenReturn(company);
        when(companyRepository.save(company)).thenReturn(savedCompany);
        when(companyMapper.toDTO(savedCompany)).thenReturn(expectedDTO);

        // Act
        CompanyDTO result = companyService.createCompany(inputDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Nouvelle Entreprise", result.getCompanyName());
        assertEquals("Nouvelle Adresse", result.getAddress());
        verify(companyMapper, times(1)).toEntity(inputDTO);
        verify(companyRepository, times(1)).save(company);
        verify(companyMapper, times(1)).toDTO(savedCompany);
    }

    @Test
    void testUpdateCompany() {
        // Arrange
        Long companyId = 1L;
        CompanyDTO updateDTO = new CompanyDTO();
        updateDTO.setCompanyName("Entreprise Modifiée");
        updateDTO.setAddress("Adresse Modifiée");
        updateDTO.setAccessInformation("Infos modifiées");

        Company existingCompany = new Company();
        existingCompany.setId(companyId);
        existingCompany.setCompanyName("Ancienne Entreprise");
        existingCompany.setAddress("Ancienne Adresse");

        Company updatedCompany = new Company();
        updatedCompany.setId(companyId);
        updatedCompany.setCompanyName("Entreprise Modifiée");
        updatedCompany.setAddress("Adresse Modifiée");
        updatedCompany.setAccessInformation("Infos modifiées");

        CompanyDTO expectedDTO = new CompanyDTO();
        expectedDTO.setId(companyId);
        expectedDTO.setCompanyName("Entreprise Modifiée");
        expectedDTO.setAddress("Adresse Modifiée");
        expectedDTO.setAccessInformation("Infos modifiées");

        when(companyRepository.findById(companyId)).thenReturn(Optional.of(existingCompany));
        when(companyRepository.save(any(Company.class))).thenReturn(updatedCompany);
        when(companyMapper.toDTO(updatedCompany)).thenReturn(expectedDTO);

        // Act
        CompanyDTO result = companyService.updateCompany(companyId, updateDTO);

        // Assert
        assertNotNull(result);
        assertEquals(companyId, result.getId());
        assertEquals("Entreprise Modifiée", result.getCompanyName());
        assertEquals("Adresse Modifiée", result.getAddress());
        assertEquals("Infos modifiées", result.getAccessInformation());
        verify(companyRepository, times(1)).findById(companyId);
        verify(companyRepository, times(1)).save(any(Company.class));
        verify(companyMapper, times(1)).toDTO(updatedCompany);
    }

    @Test
    void testDeleteCompany() {
        // Arrange
        Long companyId = 1L;
        Company company = new Company();
        company.setId(companyId);
        company.setCompanyName("Entreprise à supprimer");

        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        doNothing().when(companyRepository).delete(company);

        // Act
        companyService.deleteCompany(companyId);

        // Assert
        verify(companyRepository, times(1)).findById(companyId);
        verify(companyRepository, times(1)).delete(company);
    }
}

