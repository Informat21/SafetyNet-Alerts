package com.safetynet.alerts.service;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.DataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PhoneAlertServiceTest {

    @Mock
    private DataRepository dataRepository;

    @InjectMocks
    private PhoneAlertService phoneAlertService;

    @BeforeEach
    void setUp() {
        Mockito.lenient().when(dataRepository.findAllFireStations()).thenReturn(Arrays.asList(
                new FireStation("1509 Culver St", 1),
                new FireStation("29 15th St", 2)
        ));

        Mockito.lenient().when(dataRepository.findAll()).thenReturn(Arrays.asList(
                new Person("John", "Doe", "1509 Culver St", "Culver", "97451", "123-456-7890","john@example.com",1),
                new Person("Jane", "Doe", "29 15th St", "Culver", "97451", "987-654-3210", "jane@example.com",2),
                new Person("Bob", "Smith", "1509 Culver St", "Culver", "97451", "555-555-5555", "bob@example.com",2)
        ));
    }

    @Test
    void testGetPhoneNumbersByStation_ValidStation() {
        List<String> phoneNumbers = phoneAlertService.getPhoneNumbersByStation(1);
        assertEquals(2, phoneNumbers.size());
        assertEquals("123-456-7890", phoneNumbers.get(0));
    }

    @Test
    void testGetPhoneNumbersByStation_NoMatchingStation() {
        List<String> phoneNumbers = phoneAlertService.getPhoneNumbersByStation(3);
        assertEquals(0, phoneNumbers.size());
    }
}
