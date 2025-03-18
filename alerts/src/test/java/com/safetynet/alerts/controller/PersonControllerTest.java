package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.PersonService;
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
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PersonControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PersonService personService;

    @InjectMocks
    private PersonController personController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(personController).build();
    }

    @Test
    void testGetAllPersons() throws Exception {
        when(personService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/person"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void testAddPerson() throws Exception {
        doNothing().when(personService).save(any());

        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"address\":\"123 Main St\",\"city\":\"Paris\",\"zip\":\"75000\",\"phone\":\"123-456-7890\",\"email\":\"john.doe@example.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("Person added successfully"));
    }

    @Test
    void testUpdatePerson() throws Exception {
        Person existingPerson = new Person("John", "Doe", "123 Main St", "Paris", "75000", "123-456-7890", "john.doe@example.com",1);
        when(personService.findByFullName("John", "Doe")).thenReturn(Optional.of(existingPerson));
        doNothing().when(personService).update(any());

        mockMvc.perform(put("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"address\":\"456 Elm St\",\"city\":\"Lyon\",\"zip\":\"69000\",\"phone\":\"987-654-3210\",\"email\":\"john.doe@newmail.com\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Person updated successfully"));
    }

    @Test
    void testUpdatePerson_NotFound() throws Exception {
        when(personService.findByFullName("John", "Doe")).thenReturn(Optional.empty());

        mockMvc.perform(put("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"address\":\"456 Elm St\",\"city\":\"Lyon\",\"zip\":\"69000\",\"phone\":\"987-654-3210\",\"email\":\"john.doe@newmail.com\"}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Person not found"));
    }

    @Test
    void testDeletePerson() throws Exception {
        Person existingPerson = new Person("John", "Doe", "123 Main St", "Paris", "75000", "123-456-7890", "john.doe@example.com",1);
        when(personService.findByFullName("John", "Doe")).thenReturn(Optional.of(existingPerson));
        doNothing().when(personService).delete("John", "Doe");

        mockMvc.perform(delete("/person")
                        .param("firstName", "John")
                        .param("lastName", "Doe"))
                .andExpect(status().isOk())
                .andExpect(content().string("Person deleted successfully"));
    }

    @Test
    void testDeletePerson_NotFound() throws Exception {
        when(personService.findByFullName("John", "Doe")).thenReturn(Optional.empty());

        mockMvc.perform(delete("/person")
                        .param("firstName", "John")
                        .param("lastName", "Doe"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Person not found"));
    }
}
