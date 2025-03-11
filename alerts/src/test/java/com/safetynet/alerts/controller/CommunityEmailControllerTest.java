package com.safetynet.alerts.controller;

import com.safetynet.alerts.service.CommunityEmailService;
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
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CommunityEmailController.class)
public class CommunityEmailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommunityEmailService communityEmailService;

    @Test
    public void testGetCommunityEmails_Success() throws Exception {
        List<String> emails = Arrays.asList("john.doe@example.com", "jane.doe@example.com");

        Mockito.when(communityEmailService.getEmailsByCity("Springfield")).thenReturn(emails);

        mockMvc.perform(MockMvcRequestBuilders.get("/communityEmail")
                        .param("city", "Springfield")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)))
                .andExpect(jsonPath("$[0]", is("john.doe@example.com")))
                .andExpect(jsonPath("$[1]", is("jane.doe@example.com")));
    }

    @Test
    public void testGetCommunityEmails_NoResidents() throws Exception {
        Mockito.when(communityEmailService.getEmailsByCity("GhostTown")).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/communityEmail")
                        .param("city", "GhostTown")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(0)));
    }
}
