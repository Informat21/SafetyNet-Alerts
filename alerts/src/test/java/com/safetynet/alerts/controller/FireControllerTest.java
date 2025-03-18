package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.FireResponseDTO;
import com.safetynet.alerts.dto.ResidentInfoDTO;
import com.safetynet.alerts.service.FireService;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class FireControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FireService fireService;

    @InjectMocks
    private FireController fireController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(fireController).build();
    }

    @Test
    void testGetResidentsByAddress_ShouldReturnData_WhenAddressExists() throws Exception {
        // Création d'une liste de médicaments et allergies fictives
        List<String> medications = List.of("aspirin:500mg");
        List<String> allergies = List.of("pollen");

        // Création d'une liste de résidents fictive
        List<ResidentInfoDTO> residents = List.of(
                new ResidentInfoDTO("John", "Doe", "555-1234",35, medications, allergies)
        );

        // Création correcte de FireResponseDTO avec ses arguments attendus
        FireResponseDTO mockResponse = new FireResponseDTO("3", residents);

        when(fireService.getResidentsByAddress("1509 Culver St")).thenReturn(mockResponse);

        mockMvc.perform(get("/fire")
                        .param("address", "1509 Culver St")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stationNumber").value("3"))
                .andExpect(jsonPath("$.residents[0].firstName").value("John"))
                .andExpect(jsonPath("$.residents[0].phone").value("555-1234"))
                .andExpect(jsonPath("$.residents[0].age").value(35))
                .andExpect(jsonPath("$.residents[0].medications[0]").value("aspirin:500mg"))
                .andExpect(jsonPath("$.residents[0].allergies[0]").value("pollen"));
    }


    @Test
    void testGetResidentsByAddress_ShouldReturnNotFound_WhenNoDataExists() throws Exception {
        when(fireService.getResidentsByAddress("Unknown Address")).thenReturn(null);

        mockMvc.perform(get("/fire")
                        .param("address", "Unknown Address")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());  // Vérifie que la réponse est 404 Not Found
    }
}
