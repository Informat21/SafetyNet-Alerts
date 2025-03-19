package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.DataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceImplTest {

    @Mock
    private DataRepository dataRepository;

    @InjectMocks
    private PersonServiceImpl personService;

    private Person person1;
    private Person person2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialisation des objets de test
        person1 = new Person("John", "Doe", "123 Main St", "City", "12345", "john.doe@email.com", "123-456-7890",1);
        person2 = new Person("Jane", "Doe", "456 Oak St", "City", "12345", "jane.doe@email.com", "987-654-3210",2);
    }

    @Test
    void testFindAll() {
        when(dataRepository.findAll()).thenReturn(Arrays.asList(person1, person2));

        List<Person> result = personService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(person1));
        assertTrue(result.contains(person2));

        verify(dataRepository).findAll();
    }

    @Test
    void testFindByFullName_PersonExists() {
        when(dataRepository.findByFullName("John", "Doe")).thenReturn(Optional.of(person1));

        Optional<Person> result = personService.findByFullName("John", "Doe");

        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
        assertEquals("Doe", result.get().getLastName());

        verify(dataRepository).findByFullName("John", "Doe");
    }

    @Test
    void testFindByFullName_PersonNotExists() {
        when(dataRepository.findByFullName("Alice", "Smith")).thenReturn(Optional.empty());

        Optional<Person> result = personService.findByFullName("Alice", "Smith");

        assertFalse(result.isPresent());

        verify(dataRepository).findByFullName("Alice", "Smith");
    }

    @Test
    void testSave() {
        personService.save(person1);

        verify(dataRepository).save(person1);
    }

    @Test
    void testUpdate_PersonExists() {
        when(dataRepository.findByFullName("John", "Doe")).thenReturn(Optional.of(person1));

        personService.update(person1);

        verify(dataRepository).update(person1);
    }

    @Test
    void testUpdate_PersonNotExists() {
        when(dataRepository.findByFullName("Alice", "Smith")).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> personService.update(new Person("Alice", "Smith", "", "", "", "", "",1)));

        assertEquals("Person not found", exception.getMessage());

        verify(dataRepository).findByFullName("Alice", "Smith");
        verify(dataRepository, never()).update(any());
    }

    @Test
    void testDelete_PersonExists() {
        when(dataRepository.findByFullName("John", "Doe")).thenReturn(Optional.of(person1));

        personService.delete("John", "Doe");

        verify(dataRepository).delete("John", "Doe");
    }

    @Test
    void testDelete_PersonNotExists() {
        when(dataRepository.findByFullName("Alice", "Smith")).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> personService.delete("Alice", "Smith"));

        assertEquals("Person not found", exception.getMessage());

        verify(dataRepository).findByFullName("Alice", "Smith");
        verify(dataRepository, never()).delete(any(), any());
    }
}
