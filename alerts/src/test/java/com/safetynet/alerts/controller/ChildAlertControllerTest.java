package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.ChildAlertResponse;
import com.safetynet.alerts.service.ChildAlertService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ChildAlertControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ChildAlertService childAlertService;

    @InjectMocks
    private ChildAlertController childAlertController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(childAlertController).build();
    }

    @Test
    void testGetChildrenAlert_Success() throws Exception {
        // Mock du service
        ChildAlertResponse response = new ChildAlertResponse();
        when(childAlertService.getChildrenByAddress("123 Main St")).thenReturn(response);

        // Exécution du test
        mockMvc.perform(get("/childAlert")
                        .param("address", "123 Main St")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testGetChildrenAlert_NotFound() throws Exception {
        // Mock du service retournant null ou une réponse vide
        when(childAlertService.getChildrenByAddress("Unknown Address")).thenReturn(null);

        // Exécution du test
        mockMvc.perform(get("/childAlert")
                        .param("address", "Unknown Address")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); // Ou isNotFound() selon l'implémentation du service
    }
}
