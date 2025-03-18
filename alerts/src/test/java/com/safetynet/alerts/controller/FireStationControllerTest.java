package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.ChildAlertResponse;
import com.safetynet.alerts.dto.FireStationCoverageResponse;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.service.FireStationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class FireStationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FireStationService fireStationService;

    @InjectMocks
    private FireStationController fireStationController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(fireStationController).build();
    }

    @Test
    void testGetAllFireStations() throws Exception {
        when(fireStationService.getAllFireStations()).thenReturn(List.of(new FireStation("123 Main St", 1)));

        mockMvc.perform(get("/firestation"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].address").value("123 Main St"));
    }

    @Test
    void testAddFireStation() throws Exception {
        doNothing().when(fireStationService).addFireStation(any(FireStation.class));

        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"address\":\"123 Main St\",\"station\":1}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Mapping caserne/adresse ajouté avec succès."));
    }

    @Test
    void testUpdateFireStation() throws Exception {
        doNothing().when(fireStationService).updateFireStation(any(FireStation.class));

        mockMvc.perform(put("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"address\":\"123 Main St\",\"station\":1}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Numéro de caserne mis à jour avec succès."));
    }

    @Test
    void testDeleteFireStation() throws Exception {
        doNothing().when(fireStationService).deleteFireStation(anyString());

        mockMvc.perform(delete("/firestation").param("address", "123 Main St"))
                .andExpect(status().isOk())
                .andExpect(content().string("Mapping caserne/adresse supprimé avec succès."));
    }

    @Test
    void testGetPersonsCoveredByStation() throws Exception {
        when(fireStationService.getPersonsCoveredByStation(1))
                .thenReturn(new FireStationCoverageResponse(Collections.emptyList(), 0, 0));

        mockMvc.perform(get("/firestation?stationNumber=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfAdults").value(0))
                .andExpect(jsonPath("$.numberOfChildren").value(0));
    }

    @Test
    void testGetChildrenAtAddress() throws Exception {
        when(fireStationService.getChildrenAtAddress("123 Main St"))
                .thenReturn(new ChildAlertResponse(Collections.emptyList(), Collections.emptyList()));

        mockMvc.perform(get("/firestation/childAlert?address=123 Main St"))
                .andExpect(status().isNoContent());
    }
}






















































