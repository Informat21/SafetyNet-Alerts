package com.safetynet.alerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PersonControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PersonService personService;

    @InjectMocks
    private PersonController personController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(personController).build();
    }

    @Test
    public void testAddPerson() throws Exception {
        Person person = new Person("John", "Doe", "123 Main St", "New York", "10001","123-456-7890","john.doe@example.com", 1);

        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Person added successfully"));

        verify(personService, times(1)).save(person);
    }

    @Test
    public void testUpdatePerson() throws Exception {
        Person existingPerson = new Person("John", "Doe", "123 Main St", "New York", "10001","123-456-7890","john.doe@example.com", 1);
        Person updatedPerson = new Person("John", "Doe", "456 Elm St","New York", "10001", "987-654-3210","john.doe@example.com", 1);

        when(personService.findByFullName("John", "Doe")).thenReturn(java.util.Optional.of(existingPerson));

        mockMvc.perform(put("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPerson)))
                .andExpect(status().isOk())
                .andExpect(content().string("Person updated successfully"));

        verify(personService, times(1)).update(updatedPerson);
    }

    @Test
    public void testUpdatePersonNotFound() throws Exception {
        Person updatedPerson = new Person("John", "Doe", "456 Elm St","New York", "10001", "987-654-3210","john.doe@example.com", 1);

        when(personService.findByFullName("John", "Doe")).thenReturn(java.util.Optional.empty());

        mockMvc.perform(put("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPerson)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Person not found"));

        verify(personService, times(0)).update(updatedPerson);
    }

    @Test
    public void testDeletePerson() throws Exception {
        Person person = new Person("John", "Doe", "123 Main St", "New York", "10001","123-456-7890","john.doe@example.com", 1);

        when(personService.findByFullName("John", "Doe")).thenReturn(java.util.Optional.of(person));

        mockMvc.perform(delete("/person?firstName=John&lastName=Doe"))
                .andExpect(status().isOk())
                .andExpect(content().string("Person deleted successfully"));

        verify(personService, times(1)).delete("John", "Doe");
    }

    @Test
    public void testDeletePersonNotFound() throws Exception {
        when(personService.findByFullName("John", "Doe")).thenReturn(java.util.Optional.empty());

        mockMvc.perform(delete("/person?firstName=John&lastName=Doe"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Person not found"));

        verify(personService, times(0)).delete("John", "Doe");
    }
}
