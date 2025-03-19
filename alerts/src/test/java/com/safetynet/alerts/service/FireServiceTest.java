/*package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.FireResponseDTO;
import com.safetynet.alerts.dto.ResidentInfoDTO;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.DataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FireServiceTest {

    @Mock
    private DataRepository dataRepository;

    @InjectMocks
    private FireService fireService;

    private static final String TEST_ADDRESS = "123 Main St";
    private static final String STATION_NUMBER = "2";

    private List<Person> mockResidents;
    private MedicalRecord medicalRecord1, medicalRecord2;

    @BeforeEach
    void setUp() {
        // Mock des personnes
        mockResidents = Arrays.asList(
                new Person("Alice", "Smith", TEST_ADDRESS, "Culver", "12345", "555-1234", "alice@example.com", 2),
                new Person("Bob", "Brown", TEST_ADDRESS, "Culver", "12345", "555-5678", "bob@example.com", 2)
        );

        // Mock des dossiers médicaux
        medicalRecord1 = new MedicalRecord("Alice", "Smith", "01/01/1985", List.of("Aspirin"), List.of("Peanuts"));
        medicalRecord2 = new MedicalRecord("Bob", "Brown", "01/01/2015", List.of("Ibuprofen"), List.of());

        when(dataRepository.findPersonsByAddress(TEST_ADDRESS)).thenReturn(mockResidents);
        when(dataRepository.findStationNumberByAddress(TEST_ADDRESS)).thenReturn(Optional.of(STATION_NUMBER));
        when(dataRepository.findMedicalRecordByFullName("Alice", "Smith")).thenReturn(Optional.of(medicalRecord1));
        when(dataRepository.findMedicalRecordByFullName("Bob", "Brown")).thenReturn(Optional.of(medicalRecord2));
    }

    @Test
    void testGetResidentsByAddress_Success() {
        FireResponseDTO response = fireService.getResidentsByAddress(TEST_ADDRESS);

        assertNotNull(response);
        assertEquals(STATION_NUMBER, response.getStationNumber());
        assertEquals(2, response.getResidents().size());

        ResidentInfoDTO resident1 = response.getResidents().get(0);
        assertEquals("Alice", resident1.getFirstName());
        assertEquals("Smith", resident1.getLastName());
        assertEquals("555-1234", resident1.getPhone());
        assertEquals(40, resident1.getAge()); // En supposant que nous sommes en 2024
        assertTrue(resident1.getMedications().contains("Aspirin"));
        assertTrue(resident1.getAllergies().contains("Peanuts"));

        ResidentInfoDTO resident2 = response.getResidents().get(1);
        assertEquals("Bob", resident2.getFirstName());
        assertEquals(10, resident2.getAge()); // Né en 2015
    }

    @Test
    void testGetResidentsByAddress_NoResidents() {
        when(dataRepository.findPersonsByAddress(TEST_ADDRESS)).thenReturn(Collections.emptyList());

        FireResponseDTO response = fireService.getResidentsByAddress(TEST_ADDRESS);

        assertNull(response);
    }

    @Test
    void testGetResidentsByAddress_NoFireStation() {
        when(dataRepository.findStationNumberByAddress(TEST_ADDRESS)).thenReturn(Optional.empty());

        FireResponseDTO response = fireService.getResidentsByAddress(TEST_ADDRESS);

        assertNull(response);
    }

    @Test
    void testGetResidentsByAddress_MissingMedicalRecord() {
        when(dataRepository.findMedicalRecordByFullName("Alice", "Smith")).thenReturn(Optional.empty());

        FireResponseDTO response = fireService.getResidentsByAddress(TEST_ADDRESS);

        assertNotNull(response);
        assertEquals(2, response.getResidents().size());

        ResidentInfoDTO resident1 = response.getResidents().stream()
                .filter(res -> res.getFirstName().equals("Alice"))
                .findFirst().orElse(null);

        assertNotNull(resident1);
        assertEquals(-1, resident1.getAge()); // Âge inconnu
        assertTrue(resident1.getMedications().isEmpty());
        assertTrue(resident1.getAllergies().isEmpty());
    }
}
*/
package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.FireResponseDTO;
import com.safetynet.alerts.dto.ResidentInfoDTO;
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