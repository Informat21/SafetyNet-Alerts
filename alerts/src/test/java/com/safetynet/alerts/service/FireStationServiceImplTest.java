package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.ChildAlertResponse;
import com.safetynet.alerts.dto.FireStationCoverageResponse;
import com.safetynet.alerts.model.FireStation;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FireStationServiceImplTest {

    @Mock
    private DataRepository dataRepository;

    @InjectMocks
    private FireStationServiceImpl fireStationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllFireStations() {
        // Données simulées
        FireStation station1 = new FireStation("123 Main St", 1);
        FireStation station2 = new FireStation("456 Elm St", 2);
        when(dataRepository.findAllFireStations()).thenReturn(Arrays.asList(station1, station2));

        // Exécuter la méthode
        List<FireStation> result = fireStationService.getAllFireStations();

        // Vérification des résultats
        assertEquals(2, result.size());
        assertEquals("123 Main St", result.get(0).getAddress());
        assertEquals(1, result.get(0).getStation());
        assertEquals("456 Elm St", result.get(1).getAddress());
        assertEquals(2, result.get(1).getStation());

        // Vérification que le mock a été utilisé correctement
        verify(dataRepository).findAllFireStations();
    }

    @Test
    void testAddFireStation() {
        FireStation fireStation = new FireStation("789 Oak St", 3);
        fireStationService.addFireStation(fireStation);

        // Vérification que la méthode saveFireStation a été appelée une fois avec l'objet fireStation
        verify(dataRepository).saveFireStation(fireStation);
    }

    @Test
    void testUpdateFireStation() {
        FireStation fireStation = new FireStation("789 Oak St", 3);
        fireStationService.updateFireStation(fireStation);

        // Vérification que la méthode updateFireStation a été appelée une fois avec l'objet fireStation
        verify(dataRepository).updateFireStation(fireStation);
    }

    @Test
    void testDeleteFireStation() {
        String address = "789 Oak St";
        fireStationService.deleteFireStation(address);

        // Vérification que la méthode deleteFireStationByAddress a été appelée avec l'adresse correcte
        verify(dataRepository).deleteFireStationByAddress(address);
    }

    @Test
    void testGetFireStationByAddress() {
        String address = "123 Main St";
        FireStation station = new FireStation(address, 1);
        when(dataRepository.findAllFireStations()).thenReturn(Arrays.asList(station));

        // Exécuter la méthode
        Optional<FireStation> result = fireStationService.getFireStationByAddress(address);

        // Vérifications
        assertTrue(result.isPresent());
        assertEquals("123 Main St", result.get().getAddress());
        assertEquals(1, result.get().getStation());

        // Vérification que le mock a été utilisé correctement
        verify(dataRepository).findAllFireStations();
    }

    @Test
    void testGetPersonsCoveredByStation() {
        int stationNumber = 1;
        FireStation fireStation = new FireStation("123 Main St", stationNumber);
        Person person1 = new Person("John", "Doe", "123 Main St", "CityA", "12345", "123-456-7890", "john@example.com", stationNumber);
        Person person2 = new Person("Jane", "Doe", "123 Main St", "CityA", "12345", "098-765-4321", "jane@example.com", stationNumber);
        MedicalRecord record1 = new MedicalRecord("John", "Doe", "01/01/2000", List.of("med1"), List.of("allergy1"));
        MedicalRecord record2 = new MedicalRecord("Jane", "Doe", "02/02/2010", List.of("med2"), List.of("allergy2"));

        when(dataRepository.findAllFireStations()).thenReturn(Arrays.asList(fireStation));
        when(dataRepository.findAll()).thenReturn(Arrays.asList(person1, person2));
        when(dataRepository.getMedicalRecords()).thenReturn(Arrays.asList(record1, record2));

        // Exécuter la méthode
        FireStationCoverageResponse result = fireStationService.getPersonsCoveredByStation(stationNumber);

        // Vérifications
        assertEquals(2, result.getPersons().size());
        assertEquals(1, result.getNumberOfAdults());
        assertEquals(1, result.getNumberOfChildren());
    }

    @Test
    void testGetChildrenAtAddress() {
        String address = "123 Main St";
        Person person1 = new Person("John", "Doe", address, "CityA", "12345", "123-456-7890", "john@example.com", 1);
        Person person2 = new Person("Jane", "Doe", address, "CityA", "12345", "098-765-4321", "jane@example.com", 1);
        MedicalRecord record1 = new MedicalRecord("John", "Doe", "01/01/2015", List.of("med1"), List.of("allergy1"));  // Enfant
        MedicalRecord record2 = new MedicalRecord("Jane", "Doe", "02/02/2010", List.of("med2"), List.of("allergy2"));  // Enfant

        when(dataRepository.findAll()).thenReturn(Arrays.asList(person1, person2));
        when(dataRepository.getMedicalRecords()).thenReturn(Arrays.asList(record1, record2));

        // Exécuter la méthode
        ChildAlertResponse result = fireStationService.getChildrenAtAddress(address);

        // Vérifications
        assertEquals(2, result.getChildren().size());
        assertEquals(0, result.getOtherHouseholdMembers().size());
    }

}