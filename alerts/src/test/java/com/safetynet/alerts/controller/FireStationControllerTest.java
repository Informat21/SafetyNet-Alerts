package com.safetynet.alerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.dto.FireStationCoverageResponse;
import com.safetynet.alerts.dto.PersonInfoDTO;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.FireStationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(FireStationController.class)
class FireStationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private FireStationService fireStationService;

    @BeforeEach
    void setup() {
        List<PersonInfoDTO> persons = List.of(
                new PersonInfoDTO("John", "Doe", "123 Main St", "000-000-0000"),
                new PersonInfoDTO("Jane", "Doe", "123 Main St", "111-111-1111")
        );

        FireStationCoverageResponse response = new FireStationCoverageResponse(persons, 1, 1);

        when(fireStationService.getPersonsCoveredByStation(1)).thenReturn(response);
    }

    @InjectMocks
    private FireStationController fireStationController;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetPersonsCoveredByStation_ShouldReturnListOfPersons() throws Exception {
        // Mock des données
        PersonInfoDTO person1 = new PersonInfoDTO("John", "Doe", "123 Main St", 30, "123-456-7890", "", Collections.emptyList(), Collections.emptyList());
        PersonInfoDTO person2 = new PersonInfoDTO("Jane", "Doe", "123 Main St", 10, "123-456-7891", "", Collections.emptyList(), Collections.emptyList());

        FireStationCoverageResponse response = new FireStationCoverageResponse(Arrays.asList(person1, person2), 1, 1);

        when(fireStationService.getPersonsCoveredByStation(1)).thenReturn(response);

        // Vérification de la réponse
        mockMvc.perform(get("/firestation?stationNumber=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.persons[0].firstName").value("John"))
                .andExpect(jsonPath("$.persons[0].lastName").value("Doe"))
                .andExpect(jsonPath("$.persons[0].address").value("123 Main St"))
                .andExpect(jsonPath("$.persons[0].phone").value("123-456-7890"))
                .andExpect(jsonPath("$.adultCount").value(1))
                .andExpect(jsonPath("$.childCount").value(1));
    }

    @Test
    void testGetPersonsCoveredByStation_NoPersonsCovered_ShouldReturnEmptyList() throws Exception {
        FireStationCoverageResponse response = new FireStationCoverageResponse(Collections.emptyList(), 0, 0);

        when(fireStationService.getPersonsCoveredByStation(2)).thenReturn(response);

        mockMvc.perform(get("/firestation?stationNumber=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.persons").isEmpty())
                .andExpect(jsonPath("$.adultCount").value(0))
                .andExpect(jsonPath("$.childCount").value(0));
    }

    @Test
    void testGetPersonsCoveredByStation_StationDoesNotExist_ShouldReturnEmptyList() throws Exception {
        when(fireStationService.getPersonsCoveredByStation(99)).thenReturn(new FireStationCoverageResponse(Collections.emptyList(), 0, 0));

        mockMvc.perform(get("/firestation?stationNumber=99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.persons").isEmpty());
    }

    @Test
    void testGetPersonsCoveredByStation_InvalidStationNumber_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/firestation?stationNumber=invalid"))
                .andExpect(status().isBadRequest());
    }
}
