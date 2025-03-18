package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.FloodStationDTO;
import com.safetynet.alerts.service.FloodService;
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
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class FloodControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FloodService floodService;

    @InjectMocks
    private FloodController floodController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(floodController).build();
    }

    @Test
    void testGetHouseholdsByStations_ShouldReturnData() throws Exception {
        // Simuler une réponse de service
        FloodStationDTO floodStationDTO = new FloodStationDTO();
        floodStationDTO.setAddress("123 Main St");
        floodStationDTO.setResidents(Collections.emptyList());

        List<FloodStationDTO> responseList = Arrays.asList(floodStationDTO);

        when(floodService.getHouseholdsByStations(Arrays.asList(1, 2))).thenReturn(responseList);

        // Effectuer la requête et vérifier la réponse
        mockMvc.perform(get("/flood/stations")
                        .param("stations", "1", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].address").value("123 Main St"));

        verify(floodService, times(1)).getHouseholdsByStations(Arrays.asList(1, 2));
    }

    @Test
    void testGetHouseholdsByStations_ShouldReturnEmptyList() throws Exception {
        when(floodService.getHouseholdsByStations(Arrays.asList(3, 4))).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/flood/stations")
                        .param("stations", "3", "4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(floodService, times(1)).getHouseholdsByStations(Arrays.asList(3, 4));
    }
}
