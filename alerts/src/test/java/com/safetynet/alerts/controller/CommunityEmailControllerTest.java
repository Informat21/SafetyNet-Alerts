package com.safetynet.alerts.controller;

import com.safetynet.alerts.service.CommunityEmailService;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CommunityEmailControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CommunityEmailService communityEmailService;

    @InjectMocks
    private CommunityEmailController communityEmailController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(communityEmailController).build();
    }

    @Test
    void testGetEmailsByCity_ShouldReturnEmails_WhenEmailsExist() throws Exception {
        // Mock des emails retournés par le service
        List<String> emails = Arrays.asList("test1@example.com", "test2@example.com");
        when(communityEmailService.getEmailsByCity("Paris")).thenReturn(emails);

        // Exécution de la requête et vérification des résultats
        mockMvc.perform(get("/communityEmail")
                        .param("city", "Paris")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())  // Vérifie que le status est 200 OK
                .andExpect(content().json("[\"test1@example.com\", \"test2@example.com\"]"));  // Vérifie la réponse JSON
    }

    @Test
    void testGetEmailsByCity_ShouldReturnNoContent_WhenNoEmailsFound() throws Exception {
        // Mock d'une réponse vide
        when(communityEmailService.getEmailsByCity("UnknownCity")).thenReturn(Collections.emptyList());

        // Exécution de la requête et vérification du status 204 No Content
        mockMvc.perform(get("/communityEmail")
                        .param("city", "UnknownCity")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());  // Vérifie que le status est 204 No Content
    }
}
