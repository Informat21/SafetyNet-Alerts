package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.FireResponseDTO;
import com.safetynet.alerts.dto.ResidentInfoDTO;
import com.safetynet.alerts.service.FireService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FireController.class)
public class FireControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FireService fireService;

    @Test
    public void testGetResidentsByAddress_Success() throws Exception {
        // Données simulées
        ResidentInfoDTO resident = new ResidentInfoDTO("John", "Doe", "123-456-7890", 30, List.of("Aspirin"), List.of("Peanut"));
        FireResponseDTO response = new FireResponseDTO("2", List.of(resident));

        when(fireService.getResidentsByAddress("123 Main St")).thenReturn(response);

        // Test de la requête
        mockMvc.perform(get("/fire")
                        .param("address", "123 Main St")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stationNumber").value("2"))
                .andExpect(jsonPath("$.residents[0].firstName").value("John"))
                .andExpect(jsonPath("$.residents[0].lastName").value("Doe"))
                .andExpect(jsonPath("$.residents[0].phone").value("123-456-7890"))
                .andExpect(jsonPath("$.residents[0].age").value(30))
                .andExpect(jsonPath("$.residents[0].medications[0]").value("Aspirin"))
                .andExpect(jsonPath("$.residents[0].allergies[0]").value("Peanut"));
    }

    @Test
    public void testGetResidentsByAddress_NoResidents() throws Exception {
        when(fireService.getResidentsByAddress("456 Empty St")).thenReturn(null);

        mockMvc.perform(get("/fire")
                        .param("address", "456 Empty St")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetResidentsByAddress_InvalidAddress() throws Exception {
        when(fireService.getResidentsByAddress("invalid-address")).thenReturn(null);

        mockMvc.perform(get("/fire")
                        .param("address", "invalid-address")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
