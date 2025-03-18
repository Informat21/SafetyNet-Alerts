package com.safetynet.alerts.controller;

import com.safetynet.alerts.service.PhoneAlertService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PhoneAlertControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PhoneAlertService phoneAlertService;

    @InjectMocks
    private PhoneAlertController phoneAlertController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(phoneAlertController).build();
    }

    @Test
    void testGetPhoneNumbersByStation_WithResults() throws Exception {
        List<String> phoneNumbers = Arrays.asList("555-1234", "555-5678");
        when(phoneAlertService.getPhoneNumbersByStation(1)).thenReturn(phoneNumbers);

        mockMvc.perform(get("/phoneAlert")
                        .param("firestation", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[\"555-1234\", \"555-5678\"]"));
    }

    @Test
    void testGetPhoneNumbersByStation_NoResults() throws Exception {
        when(phoneAlertService.getPhoneNumbersByStation(2)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/phoneAlert")
                        .param("firestation", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
