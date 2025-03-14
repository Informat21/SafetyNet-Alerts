package com.safetynet.alerts.controller;

/*import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TestMedicalRecordsController {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext WebContext;

    @BeforeEach
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(WebContext).build();
    }

    @Test
    @Tag("CREATE")
    @DisplayName("ERROR CREATE Unknown Person with MedicalRecord")
    void errorCreateUnknownPersonWithMedicalRecord() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/medicalRecord").contentType(APPLICATION_JSON)
                        .content(" { \r\n" + "     \"firstName\":\"Nicolas\", \r\n" + "     \"lastName\":\"Gros\", \r\n"
                                + "     \"birthdate\":\"23/03/1993\", \r\n"
                                + "     \"medications\":[\"hydroxychloroquine:6350mg\", \"anticovid:1000mg\"], \r\n"
                                + "     \"allergies\":[\"fourmisdesneiges\"] \r\n" + "     }")
                        .accept(APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print()).andExpect(status().isConflict());
    }
}
*/


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


























