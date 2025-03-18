package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.PersonInfoDTO;
import com.safetynet.alerts.service.PersonInfoService;
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
class PersonInfoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PersonInfoService personInfoService;

    @InjectMocks
    private PersonInfoController personInfoController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(personInfoController).build();
    }

    @Test
    void testGetPersonInfo_Success() throws Exception {
        List<PersonInfoDTO> personInfoList = Arrays.asList(
                new PersonInfoDTO("John", "Doe", "123 Main St", 30, "0", "john.doe@email.com", Arrays.asList("aspirin:500mg"), Arrays.asList("peanut"))
        );

        when(personInfoService.getPersonInfoByLastName("Doe")).thenReturn(personInfoList);

        mockMvc.perform(get("/personInfolastName")
                        .param("lastName", "Doe")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"firstName\":\"John\",\"lastName\":\"Doe\",\"address\":\"123 Main St\",\"age\":30,\"email\":\"john.doe@email.com\",\"medications\":[\"aspirin:500mg\"],\"allergies\":[\"peanut\"]}]"));
    }

    @Test
    void testGetPersonInfo_NotFound() throws Exception {
        when(personInfoService.getPersonInfoByLastName("Unknown")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/personInfolastName")
                        .param("lastName", "Unknown")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}
