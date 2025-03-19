package com.safetynet.alerts.service;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.repository.DataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicalRecordServiceTest {

    @Mock
    private DataRepository dataRepository;

    @InjectMocks
    private MedicalRecordServiceImpl medicalRecordService;

    private List<MedicalRecord> medicalRecords;

    @BeforeEach
    void setUp() {
        medicalRecords = new ArrayList<>();
        medicalRecords.add(new MedicalRecord("John", "Doe", "01/01/1985", List.of("med1", "med2"), List.of("allergy1")));
        medicalRecords.add(new MedicalRecord("Jane", "Smith", "02/02/1990", List.of(), List.of()));

        when(dataRepository.getMedicalRecords()).thenReturn(medicalRecords);
    }

    @Test
    void testGetAllMedicalRecords() {
        List<MedicalRecord> result = medicalRecordService.getAllMedicalRecords();
        assertEquals(2, result.size());
    }

    @Test
    void testAddMedicalRecord() {
        MedicalRecord newRecord = new MedicalRecord("Bob", "Brown", "03/03/2000", List.of("med3"), List.of("allergy2"));

        medicalRecordService.addMedicalRecord(newRecord);

        assertTrue(medicalRecords.contains(newRecord));
    }

    @Test
    void testUpdateMedicalRecord() {
        MedicalRecord updatedRecord = new MedicalRecord("John", "Doe", "01/01/1986", List.of("med1", "med3"), List.of("allergy2"));

        medicalRecordService.updateMedicalRecord(updatedRecord);

        Optional<MedicalRecord> updated = medicalRecords.stream()
                .filter(mr -> mr.getFirstName().equals("John") && mr.getLastName().equals("Doe"))
                .findFirst();

        assertTrue(updated.isPresent());
        assertEquals("01/01/1986", updated.get().getBirthdate());
        assertTrue(updated.get().getMedications().contains("med3"));
        assertTrue(updated.get().getAllergies().contains("allergy2"));
    }

    @Test
    void testDeleteMedicalRecord() {
        medicalRecordService.deleteMedicalRecord("Jane", "Smith");

        assertEquals(1, medicalRecords.size());
        assertFalse(medicalRecords.stream().anyMatch(mr -> mr.getFirstName().equals("Jane") && mr.getLastName().equals("Smith")));
    }

    @Test
    void testGetMedicalRecordByFullName_Found() {
        Optional<MedicalRecord> result = medicalRecordService.getMedicalRecordByFullName("John", "Doe");

        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
        assertEquals("Doe", result.get().getLastName());
    }

    @Test
    void testGetMedicalRecordByFullName_NotFound() {
        Optional<MedicalRecord> result = medicalRecordService.getMedicalRecordByFullName("Alice", "Brown");

        assertFalse(result.isPresent());
    }
}
