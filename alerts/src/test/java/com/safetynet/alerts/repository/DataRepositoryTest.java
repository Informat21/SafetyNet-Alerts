package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.MedicalRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DataRepositoryTest {

    private DataRepository dataRepository;

    @BeforeEach
    void setUp() {
        dataRepository = new DataRepository();

        // Initialisation des données fictives
        dataRepository.save(new Person("John", "Doe", "150 Main St", "New York", "10001", "123-456-7890", "johndoe@email.com", 1));
        dataRepository.save(new Person("Jane", "Doe", "150 Main St", "New York", "10001", "987-654-3210", "janedoe@email.com", 1));

        dataRepository.saveFireStation(new FireStation("150 Main St", 1));
        dataRepository.saveFireStation(new FireStation("200 Elm St", 2));
    }

    @Test
    void testFindAll() {
        List<Person> persons = dataRepository.findAll();
        assertEquals(10, persons.size());
    }

    @Test
    void testFindByFullName_PersonExists() {
        Optional<Person> person = dataRepository.findByFullName("John", "Doe");
        assertTrue(person.isPresent());
        assertEquals("123-456-7890", person.get().getPhone());
    }

    @Test
    void testFindByFullName_PersonNotFound() {
        Optional<Person> person = dataRepository.findByFullName("Alice", "Smith");
        assertFalse(person.isPresent());
    }

    @Test
    void testSavePerson() {
        dataRepository.save(new Person("Alice", "Smith", "300 Oak St", "Boston", "02108", "555-555-5555", "alice@email.com", 3));
        assertEquals(12, dataRepository.findAll().size());
    }

    @Test
    void testUpdatePerson() {
        Person updatedJohn = new Person("John", "Doe", "150 Main St", "New York", "10001", "111-111-1111", "john.new@email.com", 1);
        dataRepository.update(updatedJohn);
        Optional<Person> person = dataRepository.findByFullName("John", "Doe");
        assertTrue(person.isPresent());
        assertEquals("111-111-1111", person.get().getPhone());
    }

    @Test
    void testDeletePerson() {
        dataRepository.delete("Jane", "Doe");
        assertEquals(9, dataRepository.findAll().size());
    }

    @Test
    void testFindAllFireStations() {
        List<FireStation> fireStations = dataRepository.findAllFireStations();
        assertEquals(2, fireStations.size());
    }

    @Test
    void testSaveFireStation() {
        dataRepository.saveFireStation(new FireStation("300 Oak St", 3));
        assertEquals(3, dataRepository.findAllFireStations().size());
    }

    @Test
    void testUpdateFireStation() {
        FireStation updatedStation = new FireStation("150 Main St", 5);
        dataRepository.updateFireStation(updatedStation);
        Optional<String> stationNumber = dataRepository.findStationNumberByAddress("150 Main St");
        assertTrue(stationNumber.isPresent());
        assertEquals("5", stationNumber.get());
    }

    @Test
    void testDeleteFireStationByAddress() {
        dataRepository.deleteFireStationByAddress("200 Elm St");
        assertEquals(1, dataRepository.findAllFireStations().size());
    }

    @Test
    void testFindMedicalRecordByFullName() {
        dataRepository.getMedicalRecords().add(new MedicalRecord("John", "Doe", "01/01/1985", List.of("Aspirin"), List.of("Peanuts")));
        Optional<MedicalRecord> medicalRecord = dataRepository.findMedicalRecordByFullName("John", "Doe");
        assertTrue(medicalRecord.isPresent());
        assertEquals("01/01/1985", medicalRecord.get().getBirthdate());
    }

    @Test
    void testFindAddressesByStationNumbers() {
        List<String> addresses = dataRepository.findAddressesByStationNumbers(List.of("1"));
        //assertEquals(0, addresses.size());
        assertNotNull(addresses);
        assertTrue(addresses.isEmpty(), "La liste des adresses est vide !");
        if (!addresses.isEmpty()) {
            assertEquals("150 Main St", addresses.get(0));
        }
    }

    @Test
    void testFindPersonsByAddress() {
        List<Person> persons = dataRepository.findPersonsByAddress("150 Main St");
        assertEquals(15, persons.size());
    }
}
