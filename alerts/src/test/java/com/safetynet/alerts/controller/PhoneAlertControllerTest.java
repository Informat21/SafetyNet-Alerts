package com.safetynet.alerts.controller;

import com.safetynet.alerts.service.PhoneAlertService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PhoneAlertController.class)
public class PhoneAlertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PhoneAlertService phoneAlertService;

    @Test
    public void testGetPhoneNumbersByStation_Success() throws Exception {
        // Données simulées
        int stationNumber = 1;
        Mockito.when(phoneAlertService.getPhoneNumbersByStation(stationNumber))
                .thenReturn(Arrays.asList("123-456-7890", "987-654-3210"));

        // Appel MockMvc et vérification des résultats
        mockMvc.perform(MockMvcRequestBuilders.get("/phoneAlert")
                        .param("firestation", String.valueOf(stationNumber))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", containsInAnyOrder("123-456-7890", "987-654-3210")))
                .andDo(print());
    }

    @Test
    public void testGetPhoneNumbersByStation_NoContent() throws Exception {
        // Cas où aucune personne n'est desservie
        int stationNumber = 2;
        Mockito.when(phoneAlertService.getPhoneNumbersByStation(stationNumber))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/phoneAlert")
                        .param("firestation", String.valueOf(stationNumber))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()) // Vérifie que le statut est 204
                .andDo(print());
    }
}
