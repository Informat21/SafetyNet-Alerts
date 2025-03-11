package com.safetynet.alerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class MedicalRecordControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MedicalRecordService medicalRecordService;

    @InjectMocks
    private MedicalRecordController medicalRecordController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(medicalRecordController).build();
    }

    @Test
    public void testAddMedicalRecord() throws Exception {
        MedicalRecord medicalRecord = new MedicalRecord("John", "Doe", "01/01/1990",
                List.of("Aspirin"), List.of("Peanuts"));

        mockMvc.perform(post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicalRecord)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Dossier médical ajouté avec succès."));

        verify(medicalRecordService, times(1)).addMedicalRecord(medicalRecord);
    }

    @Test
    public void testUpdateMedicalRecord() throws Exception {
        MedicalRecord existingRecord = new MedicalRecord("John", "Doe", "01/01/1990",
                List.of("Aspirin"), List.of("Peanuts"));
        MedicalRecord updatedRecord = new MedicalRecord("John", "Doe", "02/02/1991",
                List.of("Ibuprofen"), List.of("Shellfish"));

        when(medicalRecordService.getMedicalRecordByFullName("John", "Doe")).thenReturn(Optional.of(existingRecord));

        mockMvc.perform(put("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedRecord)))
                .andExpect(status().isOk())
                .andExpect(content().string("Dossier médical mis à jour avec succès."));

        verify(medicalRecordService, times(1)).updateMedicalRecord(updatedRecord);
    }

    @Test
    public void testUpdateMedicalRecordNotFound() throws Exception {
        MedicalRecord updatedRecord = new MedicalRecord("John", "Doe", "02/02/1991",
                List.of("Ibuprofen"), List.of("Shellfish"));

        when(medicalRecordService.getMedicalRecordByFullName("John", "Doe")).thenReturn(Optional.empty());

        mockMvc.perform(put("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedRecord)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Dossier médical non trouvé."));

        verify(medicalRecordService, times(0)).updateMedicalRecord(updatedRecord);
    }

    @Test
    public void testDeleteMedicalRecord() throws Exception {
        MedicalRecord medicalRecord = new MedicalRecord("John", "Doe", "01/01/1990",
                List.of("Aspirin"), List.of("Peanuts"));

        when(medicalRecordService.getMedicalRecordByFullName("John", "Doe")).thenReturn(Optional.of(medicalRecord));

        mockMvc.perform(delete("/medicalRecord?firstName=John&lastName=Doe"))
                .andExpect(status().isOk())
                .andExpect(content().string("Dossier médical supprimé avec succès."));

        verify(medicalRecordService, times(1)).deleteMedicalRecord("John", "Doe");
    }

    @Test
    public void testDeleteMedicalRecordNotFound() throws Exception {
        when(medicalRecordService.getMedicalRecordByFullName("John", "Doe")).thenReturn(Optional.empty());

        mockMvc.perform(delete("/medicalRecord?firstName=John&lastName=Doe"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Dossier médical non trouvé."));

        verify(medicalRecordService, times(0)).deleteMedicalRecord("John", "Doe");
    }
}
