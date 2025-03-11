package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.FloodStationDTO;
import com.safetynet.alerts.service.FloodService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;

@WebMvcTest(FloodController.class)
class FloodControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FloodService floodService;

    @Test
    void testGetHouseholdsByStations_ReturnsHouseholds() throws Exception {
        // Given
        FloodStationDTO floodStationDTO = new FloodStationDTO();
        floodStationDTO.setAddress("1509 Culver St");
        floodStationDTO.setResidents(List.of());

        List<FloodStationDTO> response = List.of(floodStationDTO);
        when(floodService.getHouseholdsByStations(List.of(1, 2))).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/flood/stations")
                        .param("stations", "1", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("1509 Culver St")));
    }

    @Test
    void testGetHouseholdsByStations_ReturnsEmptyList() throws Exception {
        // Given
        when(floodService.getHouseholdsByStations(List.of(1, 2))).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/flood/stations")
                        .param("stations", "1", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void testGetHouseholdsByStations_WithNonExistentStations() throws Exception {
        // Given
        when(floodService.getHouseholdsByStations(List.of(99, 100))).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/flood/stations")
                        .param("stations", "99", "100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}
