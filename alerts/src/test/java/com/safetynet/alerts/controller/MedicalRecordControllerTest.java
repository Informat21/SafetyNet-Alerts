package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.service.MedicalRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@ExtendWith(MockitoExtension.class)
class MedicalRecordControllerTest {

    private MockMvc mockMvc;
    @Mock
    private MedicalRecordService medicalRecordService;

    @InjectMocks  // Injecte le mock dans le contrôleur
    private MedicalRecordController medicalRecordController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(medicalRecordController).build();
    }
    @Test
    void testGetAllMedicalRecords() throws Exception {
        when(medicalRecordService.getAllMedicalRecords()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/medicalRecord"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void testAddMedicalRecord() throws Exception {
        MedicalRecord medicalRecord = new MedicalRecord("John", "Doe", "01/01/1990", Arrays.asList("aspirin:500mg"), Arrays.asList("pollen"));
        doNothing().when(medicalRecordService).addMedicalRecord(any());

        mockMvc.perform(post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"birthdate\":\"01/01/1990\",\"medications\":[\"aspirin:500mg\"],\"allergies\":[\"pollen\"]}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("Dossier médical ajouté avec succès."));
    }

    @Test
    void testUpdateMedicalRecord() throws Exception {
        MedicalRecord medicalRecord = new MedicalRecord("John", "Doe", "01/01/1990", Arrays.asList("aspirin:500mg"), Arrays.asList("pollen"));
        when(medicalRecordService.getMedicalRecordByFullName("John", "Doe")).thenReturn(Optional.of(medicalRecord));
        doNothing().when(medicalRecordService).updateMedicalRecord(any());

        mockMvc.perform(put("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"birthdate\":\"01/01/1990\",\"medications\":[\"aspirin:500mg\"],\"allergies\":[\"pollen\"]}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Dossier médical mis à jour avec succès."));
    }

    @Test
    void testDeleteMedicalRecord() throws Exception {
        MedicalRecord medicalRecord = new MedicalRecord("John", "Doe", "01/01/1990", Arrays.asList("aspirin:500mg"), Arrays.asList("pollen"));
        when(medicalRecordService.getMedicalRecordByFullName("John", "Doe")).thenReturn(Optional.of(medicalRecord));
        doNothing().when(medicalRecordService).deleteMedicalRecord("John", "Doe");

        mockMvc.perform(delete("/medicalRecord")
                        .param("firstName", "John")
                        .param("lastName", "Doe"))
                .andExpect(status().isOk())
                .andExpect(content().string("Dossier médical supprimé avec succès."));
    }
}


























