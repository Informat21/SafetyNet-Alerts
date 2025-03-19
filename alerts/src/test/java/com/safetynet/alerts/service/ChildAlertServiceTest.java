package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.ChildAlertResponse;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.DataRepository;
import com.safetynet.alerts.utils.DateUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.MockedStatic;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChildAlertServiceTest {

    @Mock
    private DataRepository dataRepository;

    @InjectMocks
    private ChildAlertService childAlertService;

    private static final String TEST_ADDRESS = "123 Main St";

    @BeforeEach
    void setUp() {
        // Assurer que les méthodes statiques sont bien mockées pour chaque test
    }

    @Test
    void testGetChildrenByAddress_WithChildren() {
        // Mock des personnes à l'adresse
        Person child1 = new Person("Alice", "Smith", TEST_ADDRESS, "Culver", "12345", "555-1234", "alice@example.com", 1);
        Person child2 = new Person("Bob", "Smith", TEST_ADDRESS, "Culver", "12345", "555-5678", "bob@example.com", 1);
        Person adult = new Person("John", "Smith", TEST_ADDRESS, "Culver", "12345", "555-0000", "john@example.com", 1);

        List<Person> personsAtAddress = Arrays.asList(child1, child2, adult);
        when(dataRepository.findPersonsByAddress(TEST_ADDRESS)).thenReturn(personsAtAddress);

        // Mock des dossiers médicaux avec format MM/dd/yyyy
        when(dataRepository.findMedicalRecordByFullName("Alice", "Smith"))
                .thenReturn(Optional.of(new MedicalRecord("Alice", "Smith", "01/01/2015", null, null)));
        when(dataRepository.findMedicalRecordByFullName("Bob", "Smith"))
                .thenReturn(Optional.of(new MedicalRecord("Bob", "Smith", "01/01/2017", null, null)));
        when(dataRepository.findMedicalRecordByFullName("John", "Smith"))
                .thenReturn(Optional.of(new MedicalRecord("John", "Smith", "01/01/1980", null, null)));

        // Mock du calcul d'âge avec bloc try-with-resources pour bien gérer MockedStatic
        try (MockedStatic<DateUtils> mockedDateUtils = mockStatic(DateUtils.class)) {
            mockedDateUtils.when(() -> DateUtils.calculateAge("01/01/2015")).thenReturn(9);
            mockedDateUtils.when(() -> DateUtils.calculateAge("01/01/2017")).thenReturn(7);
            mockedDateUtils.when(() -> DateUtils.calculateAge("01/01/1980")).thenReturn(44);

            // Exécution de la méthode testée
            ChildAlertResponse response = childAlertService.getChildrenByAddress(TEST_ADDRESS);

            // Vérifications
            assertNotNull(response);
            assertEquals(2, response.getChildren().size());
            assertEquals(1, response.getOtherHouseholdMembers().size());

            assertEquals("Alice", response.getChildren().get(0).getFirstName());
            assertEquals("Bob", response.getChildren().get(1).getFirstName());
            assertEquals("John", response.getOtherHouseholdMembers().get(0).getFirstName());
        }
    }

    @Test
    void testGetChildrenByAddress_NoChildren() {
        // Mock des adultes sans enfants
        Person adult1 = new Person("John", "Smith", TEST_ADDRESS, "Culver", "12345", "555-0000", "john@example.com", 1);
        Person adult2 = new Person("Jane", "Smith", TEST_ADDRESS, "Culver", "12345", "555-1111", "jane@example.com", 1);

        List<Person> personsAtAddress = Arrays.asList(adult1, adult2);
        when(dataRepository.findPersonsByAddress(TEST_ADDRESS)).thenReturn(personsAtAddress);

        // Mock des dossiers médicaux
        when(dataRepository.findMedicalRecordByFullName("John", "Smith"))
                .thenReturn(Optional.of(new MedicalRecord("John", "Smith", "01/01/1980", null, null)));
        when(dataRepository.findMedicalRecordByFullName("Jane", "Smith"))
                .thenReturn(Optional.of(new MedicalRecord("Jane", "Smith", "01/01/1975", null, null)));

        // Mock du calcul d'âge
        try (MockedStatic<DateUtils> mockedDateUtils = mockStatic(DateUtils.class)) {
            mockedDateUtils.when(() -> DateUtils.calculateAge("01/01/1980")).thenReturn(44);
            mockedDateUtils.when(() -> DateUtils.calculateAge("01/01/1975")).thenReturn(49);

            // Exécution de la méthode
            ChildAlertResponse response = childAlertService.getChildrenByAddress(TEST_ADDRESS);

            // Vérifications
            assertNotNull(response);
            assertTrue(response.getChildren().isEmpty());
            assertEquals(2, response.getOtherHouseholdMembers().size());
        }
    }

    @Test
    void testGetChildrenByAddress_EmptyAddress() {
        // Aucun habitant à l'adresse
        when(dataRepository.findPersonsByAddress(TEST_ADDRESS)).thenReturn(Collections.emptyList());

        // Exécution de la méthode
        ChildAlertResponse response = childAlertService.getChildrenByAddress(TEST_ADDRESS);

        // Vérifications
        assertNotNull(response);
        assertTrue(response.getChildren().isEmpty());
        assertTrue(response.getOtherHouseholdMembers().isEmpty());
    }
}
