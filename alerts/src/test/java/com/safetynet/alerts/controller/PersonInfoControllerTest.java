package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.PersonInfoDTO;
import com.safetynet.alerts.service.PersonInfoService;
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
@WebMvcTest(PersonInfoController.class)
public class PersonInfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonInfoService personInfoService;

    @Test
    public void testGetPersonInfo_Success() throws Exception {
        PersonInfoDTO person = new PersonInfoDTO();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setAddress("123 Main St");
        person.setEmail("john.doe@example.com");
        person.setAge(30);
        person.setMedications(Arrays.asList("Med1", "Med2"));
        person.setAllergies(Arrays.asList("Peanuts"));

        List<PersonInfoDTO> response = Collections.singletonList(person);

        Mockito.when(personInfoService.getPersonInfoByLastName("Doe")).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/personInfolastName")
                        .param("lastName", "Doe")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].firstName", is("John")))
                .andExpect(jsonPath("$[0].lastName", is("Doe")))
                .andExpect(jsonPath("$[0].address", is("123 Main St")))
                .andExpect(jsonPath("$[0].email", is("john.doe@example.com")))
                .andExpect(jsonPath("$[0].age", is(30)))
                .andExpect(jsonPath("$[0].medications", hasSize(2)))
                .andExpect(jsonPath("$[0].allergies", hasSize(1)));
    }

    @Test
    public void testGetPersonInfo_MultiplePeopleSameLastName() throws Exception {
        PersonInfoDTO person1 = new PersonInfoDTO();
        person1.setFirstName("John");
        person1.setLastName("Smith");
        person1.setAddress("123 Oak St");
        person1.setEmail("john.smith@example.com");
        person1.setAge(40);

        PersonInfoDTO person2 = new PersonInfoDTO();
        person2.setFirstName("Jane");
        person2.setLastName("Smith");
        person2.setAddress("456 Elm St");
        person2.setEmail("jane.smith@example.com");
        person2.setAge(35);

        List<PersonInfoDTO> response = Arrays.asList(person1, person2);

        Mockito.when(personInfoService.getPersonInfoByLastName("Smith")).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/personInfolastName")
                        .param("lastName", "Smith")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)))
                .andExpect(jsonPath("$[0].firstName", is("John")))
                .andExpect(jsonPath("$[1].firstName", is("Jane")));
    }

    @Test
    public void testGetPersonInfo_NoPersonFound() throws Exception {
        Mockito.when(personInfoService.getPersonInfoByLastName("Unknown")).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/personInfolastName")
                        .param("lastName", "Unknown")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(0)));
    }
}
