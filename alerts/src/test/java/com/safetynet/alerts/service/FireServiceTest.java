
package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.FireResponseDTO;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.DataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class FireServiceTest {

    @Mock
    private DataRepository dataRepository;

    @InjectMocks
    private FireService fireService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetResidentsByAddress_WithResidentsAndStationNumber() {
        // Données simulées
        String address = "123 Main St";
        Person person1 = new Person("John", "Doe", address, "City", "12345","123-456-7890","john.doe@example.com", 1);
        Person person2 = new Person("Jane", "Doe", address, "City", "12345", "098-765-4321","jane.doe@example.com",1);
        MedicalRecord medicalRecord1 = new MedicalRecord("John", "Doe", "01/01/1990", List.of("med1"), List.of("allergy1"));
        MedicalRecord medicalRecord2 = new MedicalRecord("Jane", "Doe", "02/02/1992", List.of("med2"), List.of("allergy2"));

        // Comportement attendu du mock
        when(dataRepository.findPersonsByAddress(address)).thenReturn(Arrays.asList(person1, person2));
        when(dataRepository.findStationNumberByAddress(address)).thenReturn(Optional.of("1"));
        when(dataRepository.findMedicalRecordByFullName("John", "Doe")).thenReturn(Optional.of(medicalRecord1));
        when(dataRepository.findMedicalRecordByFullName("Jane", "Doe")).thenReturn(Optional.of(medicalRecord2));

        // Appel de la méthode que nous testons
        FireResponseDTO responseDTO = fireService.getResidentsByAddress(address);

        // Assertions
        assertEquals("1", responseDTO.getStationNumber());
        assertEquals(2, responseDTO.getResidents().size());
        assertEquals("John", responseDTO.getResidents().get(0).getFirstName());
        assertEquals(35, responseDTO.getResidents().get(0).getAge()); // Assurez-vous que l'âge est correct
        assertEquals("Jane", responseDTO.getResidents().get(1).getFirstName());
        assertEquals(33, responseDTO.getResidents().get(1).getAge()); // Assurez-vous que l'âge est correct

        // Vérifications
        verify(dataRepository).findPersonsByAddress(address);
        verify(dataRepository).findStationNumberByAddress(address);
        verify(dataRepository, times(2)).findMedicalRecordByFullName(anyString(), anyString());
    }

    @Test
    void testGetResidentsByAddress_NoResidents() {
        String address = "Nonexistent Address";

        // Comportement attendu du mock
        when(dataRepository.findPersonsByAddress(address)).thenReturn(Arrays.asList());
        when(dataRepository.findStationNumberByAddress(address)).thenReturn(Optional.empty());

        // Appel de la méthode que nous testons
        FireResponseDTO responseDTO = fireService.getResidentsByAddress(address);

        // Assertion
        assertNull(responseDTO);

        // Vérifications
        verify(dataRepository).findPersonsByAddress(address);
        verify(dataRepository).findStationNumberByAddress(address);
    }

    @Test
    void testGetResidentsByAddress_NoStationNumber() {
        String address = "123 Main St";
        Person person = new Person("John", "Doe", address, "City", "12345", "123-456-7890", "john.doe@example.com",1);

        // Comportement attendu du mock
        when(dataRepository.findPersonsByAddress(address)).thenReturn(Arrays.asList(person));
        when(dataRepository.findStationNumberByAddress(address)).thenReturn(Optional.empty());

        // Appel de la méthode que nous testons
        FireResponseDTO responseDTO = fireService.getResidentsByAddress(address);

        // Assertion
        assertNull(responseDTO);

        // Vérifications
        verify(dataRepository).findPersonsByAddress(address);
        verify(dataRepository).findStationNumberByAddress(address);
    }
}