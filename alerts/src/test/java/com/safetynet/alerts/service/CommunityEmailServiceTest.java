package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.DataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommunityEmailServiceTest {

    @Mock
    private DataRepository dataRepository;

    @InjectMocks
    private CommunityEmailService communityEmailService;

    private List<Person> mockPersons;

    @BeforeEach
    void setUp() {
        mockPersons = Arrays.asList(
                new Person("Alice", "Smith", "123 Main St", "Culver", "12345", "555-1234", "alice@example.com", 1),
                new Person("Bob", "Brown", "456 Oak St", "Culver", "12345", "555-5678", "bob@example.com", 2),
                new Person("Charlie", "Davis", "789 Pine St", "Culver", "12345", "555-9876", "charlie@example.com", 3),
                new Person("Alice", "Smith", "123 Main St", "Culver", "12345", "555-1234", "alice@example.com", 1), // Doublon
                new Person("David", "Wilson", "101 Maple St", "Springfield", "54321", "555-1111", "david@example.com", 4)
        );
    }

    @Test
    void testGetEmailsByCity_WithResults() {
        when(dataRepository.findAll()).thenReturn(mockPersons);

        List<String> emails = communityEmailService.getEmailsByCity("Culver");

        assertNotNull(emails);
        assertEquals(3, emails.size()); // Un doublon est supprimé
        assertTrue(emails.containsAll(List.of("alice@example.com", "bob@example.com", "charlie@example.com")));

        verify(dataRepository, times(1)).findAll();
    }

    @Test
    void testGetEmailsByCity_NoResults() {
        when(dataRepository.findAll()).thenReturn(mockPersons);

        List<String> emails = communityEmailService.getEmailsByCity("UnknownCity");

        assertNotNull(emails);
        assertTrue(emails.isEmpty());

        verify(dataRepository, times(1)).findAll();
    }

    @Test
    void testGetEmailsByCity_CaseInsensitive() {
        when(dataRepository.findAll()).thenReturn(mockPersons);

        List<String> emailsLowercase = communityEmailService.getEmailsByCity("culver");
        List<String> emailsUppercase = communityEmailService.getEmailsByCity("CULVER");

        assertEquals(3, emailsLowercase.size());
        assertEquals(emailsLowercase, emailsUppercase);

        verify(dataRepository, times(2)).findAll();
    }

    @Test
    void testGetEmailsByCity_EmptyDatabase() {
        when(dataRepository.findAll()).thenReturn(Collections.emptyList());

        List<String> emails = communityEmailService.getEmailsByCity("Culver");

        assertNotNull(emails);
        assertTrue(emails.isEmpty());

        verify(dataRepository, times(1)).findAll();
    }
}
