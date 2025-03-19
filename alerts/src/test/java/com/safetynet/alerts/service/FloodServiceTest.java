package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.FloodStationDTO;
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
class FloodServiceTest {

    @Mock
    private DataRepository dataRepository;

    @InjectMocks
    private FloodService floodService;

    private static final List<Integer> TEST_STATION_NUMBERS = Arrays.asList(1, 2);
    private static final List<String> TEST_ADDRESSES = Arrays.asList("123 Main St", "456 Elm St");

    private List<Person> mockResidents;
    private MedicalRecord medicalRecord1, medicalRecord2;

    @BeforeEach
    void setUp() {
        // Mock des adresses desservies par les stations
        when(dataRepository.getAddressesByStation(TEST_STATION_NUMBERS)).thenReturn(TEST_ADDRESSES);

        // Mock des personnes habitant à ces adresses
        mockResidents = Arrays.asList(
                new Person("Alice", "Smith", "123 Main St", "Culver", "12345", "555-1234", "alice@example.com", 1),
                new Person("Bob", "Brown", "456 Elm St", "Culver", "12345", "555-5678", "bob@example.com", 2)
        );

        when(dataRepository.findAll()).thenReturn(mockResidents);

        // Mock des dossiers médicaux
        medicalRecord1 = new MedicalRecord("Alice", "Smith", "01/01/1985", List.of("Aspirin"), List.of("Peanuts"));
        medicalRecord2 = new MedicalRecord("Bob", "Brown", "01/01/2015", List.of("Ibuprofen"), List.of());

        lenient().when(dataRepository.findMedicalRecordByFullName("Alice", "Smith")).thenReturn(Optional.of(medicalRecord1));
        lenient().when(dataRepository.findMedicalRecordByFullName("Bob", "Brown")).thenReturn(Optional.of(medicalRecord2));
    }

    @Test
    void testGetHouseholdsByStations_Success() {
        List<FloodStationDTO> response = floodService.getHouseholdsByStations(TEST_STATION_NUMBERS);

        assertNotNull(response);
        assertEquals(2, response.size());

        FloodStationDTO household1 = response.stream().filter(h -> h.getAddress().equals("123 Main St")).findFirst().orElse(null);
        assertNotNull(household1);
        assertEquals(1, household1.getResidents().size());

        ResidentInfoDTO resident1 = household1.getResidents().get(0);
        assertEquals("Alice", resident1.getFirstName());
        assertEquals(40, resident1.getAge()); // En supposant que nous sommes en 2024
        assertTrue(resident1.getMedications().contains("Aspirin"));
        assertTrue(resident1.getAllergies().contains("Peanuts"));

        FloodStationDTO household2 = response.stream().filter(h -> h.getAddress().equals("456 Elm St")).findFirst().orElse(null);
        assertNotNull(household2);
        assertEquals(1, household2.getResidents().size());

        ResidentInfoDTO resident2 = household2.getResidents().get(0);
        assertEquals("Bob", resident2.getFirstName());
        assertEquals(10, resident2.getAge()); // Né en 2015
    }

    @Test
    void testGetHouseholdsByStations_NoAddressesFound() {
        when(dataRepository.getAddressesByStation(TEST_STATION_NUMBERS)).thenReturn(Collections.emptyList());

        List<FloodStationDTO> response = floodService.getHouseholdsByStations(TEST_STATION_NUMBERS);

        assertNotNull(response);
        assertTrue(response.isEmpty());
    }

    @Test
    void testGetHouseholdsByStations_NoResidentsFound() {
        when(dataRepository.findAll()).thenReturn(Collections.emptyList());

        List<FloodStationDTO> response = floodService.getHouseholdsByStations(TEST_STATION_NUMBERS);

        assertNotNull(response);
        assertTrue(response.isEmpty());
    }

    @Test
    void testGetHouseholdsByStations_PersonWithoutMedicalRecord() {
        when(dataRepository.findMedicalRecordByFullName("Alice", "Smith")).thenReturn(Optional.empty());

        List<FloodStationDTO> response = floodService.getHouseholdsByStations(TEST_STATION_NUMBERS);

        assertNotNull(response);
        assertEquals(2, response.size());

        FloodStationDTO household1 = response.stream().filter(h -> h.getAddress().equals("123 Main St")).findFirst().orElse(null);
        assertNotNull(household1);
        assertEquals(1, household1.getResidents().size());

        ResidentInfoDTO resident1 = household1.getResidents().get(0);
        assertEquals("Alice", resident1.getFirstName());
        assertEquals(-2, resident1.getAge()); // Âge inconnu
        assertTrue(resident1.getMedications().isEmpty());
        assertTrue(resident1.getAllergies().isEmpty());
    }
}
