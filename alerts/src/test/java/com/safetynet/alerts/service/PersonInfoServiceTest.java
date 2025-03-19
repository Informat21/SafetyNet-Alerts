package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.PersonInfoDTO;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonInfoServiceTest {

    @Mock
    private DataRepository dataRepository;

    @InjectMocks
    private PersonInfoService personInfoService;

    private Person person1;
    private Person person2;
    private MedicalRecord medicalRecord1;
    private MedicalRecord medicalRecord2;

    @BeforeEach
    void setUp() {
        person1 = new Person("John", "Doe", "123 Main St", "Springfield", "12345", "555-1234","john.doe@email.com", 1 );
        person2 = new Person("Jane", "Doe", "456 Elm St", "Springfield", "12345","555-5678", "jane.doe@email.com",1 );

        medicalRecord1 = new MedicalRecord("John", "Doe", "01/01/1980", List.of("Aspirin"), List.of("Peanut"));
        medicalRecord2 = new MedicalRecord("Jane", "Doe", "05/05/1990", List.of(), List.of("Dust"));

        lenient().when(dataRepository.findByLastName("Doe")).thenReturn(List.of(person1, person2));
        lenient().when(dataRepository.findMedicalRecordByFullName("John", "Doe")).thenReturn(Optional.of(medicalRecord1));
        lenient().when(dataRepository.findMedicalRecordByFullName("Jane", "Doe")).thenReturn(Optional.of(medicalRecord2));
    }

    @Test
    void testGetPersonInfoByLastName() {
        List<PersonInfoDTO> result = personInfoService.getPersonInfoByLastName("Doe");

        assertEquals(2, result.size());

        PersonInfoDTO dto1 = result.get(0);
        assertEquals("John", dto1.getFirstName());
        assertEquals("Doe", dto1.getLastName());
        assertEquals("123 Main St", dto1.getAddress());
        assertEquals("john.doe@email.com", dto1.getEmail());
        assertEquals(List.of("Aspirin"), dto1.getMedications());
        assertEquals(List.of("Peanut"), dto1.getAllergies());
        assertEquals(DateUtils.calculateAge("01/01/1980"), dto1.getAge());

        PersonInfoDTO dto2 = result.get(1);
        assertEquals("Jane", dto2.getFirstName());
        assertEquals("Doe", dto2.getLastName());
        assertEquals("456 Elm St", dto2.getAddress());
        assertEquals("jane.doe@email.com", dto2.getEmail());
        assertTrue(dto2.getMedications().isEmpty());
        assertEquals(List.of("Dust"), dto2.getAllergies());
        assertEquals(DateUtils.calculateAge("05/05/1990"), dto2.getAge());
    }

    @Test
    void testGetPersonInfoByLastName_NoMedicalRecord() {
        when(dataRepository.findMedicalRecordByFullName("Jane", "Doe")).thenReturn(Optional.empty());

        List<PersonInfoDTO> result = personInfoService.getPersonInfoByLastName("Doe");

        assertEquals(2, result.size());

        PersonInfoDTO dto2 = result.get(1);
        assertEquals("Jane", dto2.getFirstName());
        assertEquals("Doe", dto2.getLastName());
        assertEquals("456 Elm St", dto2.getAddress());
        assertEquals("jane.doe@email.com", dto2.getEmail());
        assertNull(dto2.getMedications());
        assertNull(dto2.getAllergies());
        assertEquals(0, dto2.getAge()); // Age par défaut
    }

    @Test
    void testGetPersonInfoByLastName_NotFound() {
        when(dataRepository.findByLastName("Smith")).thenReturn(List.of());

        List<PersonInfoDTO> result = personInfoService.getPersonInfoByLastName("Smith");

        assertTrue(result.isEmpty());
    }
}
