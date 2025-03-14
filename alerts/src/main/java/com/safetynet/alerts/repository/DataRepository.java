package com.safetynet.alerts.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.MedicalRecord;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import org.springframework.context.event.EventListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Iterator;

@Repository
@Slf4j
@Data
public class DataRepository {

    private static List<Person> persons = new ArrayList<>();
    private List<FireStation> fireStations;
    private List<MedicalRecord> medicalRecords;


    @EventListener(ApplicationReadyEvent.class)
    private void loadData() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Resource resource = new ClassPathResource("data.json");

            File file = resource.getFile();

            DataWrapper data = objectMapper.readValue(file, DataWrapper.class);
            this.persons = data.getPersons();
            this.fireStations = data.getFirestations();
            this.medicalRecords = data.getMedicalrecords();
            log.info("Données chargées avec succès depuis data.json");
            log.info("Data loaded: Persons - {}, FireStations - {}, MedicalRecords - {}",
                    persons.size(), fireStations.size(), medicalRecords.size());
        } catch (IOException e) {

            log.error("Erreur lors du chargement du fichier JSON", e);
        }
    }
    //Récupérer toutes les personnes
    public List<Person> findAll() {
        return persons;

    }

    //Trouver une personne par prénom et nom
    public static Optional<Person> findByFullName(String firstName, String lastName) {
        return persons.stream()
                .filter(p -> p.getFirstName().equalsIgnoreCase(firstName) && p.getLastName().equalsIgnoreCase(lastName))
                .findFirst();
    }

    //Ajouter une personne
    public void save(Person person) {
        persons.add(person);
    }

    //Mettre à jour une personne existante
    public void update(Person updatedPerson) {
        persons = persons.stream()
                .map(person -> {
                    if (person.getFirstName().equalsIgnoreCase(updatedPerson.getFirstName())
                            && person.getLastName().equalsIgnoreCase(updatedPerson.getLastName())) {
                        return updatedPerson;  // Remplace la personne existante par la nouvelle version
                    }
                    return person;
                })
                .collect(Collectors.toList());
    }

    //Supprimer une personne par prénom et nom
    public void delete(String firstName, String lastName) {
        persons = persons.stream()
                .filter(person -> !(person.getFirstName().equalsIgnoreCase(firstName)
                        && person.getLastName().equalsIgnoreCase(lastName)))
                .collect(Collectors.toList());
    }
    // Récupérer tous les mappings caserne/adresse
    public List<FireStation> findAllFireStations() {
        return fireStations;
    }

    // Ajouter un nouveau mapping caserne/adresse
    public void saveFireStation(FireStation fireStation) {
        fireStations.add(fireStation);
    }

    // Mettre à jour le numéro de la caserne d'une adresse existante
    public void updateFireStation(FireStation updatedFireStation) {
        fireStations.stream()
                .filter(fs -> fs.getAddress().equalsIgnoreCase(updatedFireStation.getAddress()))
                .findFirst()
                .ifPresent(fs -> fs.setStation(updatedFireStation.getStation()));
    }

    // Supprimer un mapping caserne/adresse par adresse
    public void deleteFireStationByAddress(String address) {
        Iterator<FireStation> iterator = fireStations.iterator();
        while (iterator.hasNext()) {
            FireStation fireStation = iterator.next();
            if (fireStation.getAddress().equalsIgnoreCase(address)) {
                iterator.remove();
            }
        }
    }
    @Data
    private static class DataWrapper {
        private List<Person> persons;
        private List<FireStation> firestations;
        private List<MedicalRecord> medicalrecords;
    }

    // Trouver toutes les personnes par nom de famille
    public List<Person> findByLastName(String lastName) {
        return persons.stream()
                .filter(p -> p.getLastName().equalsIgnoreCase(lastName))
                .collect(Collectors.toList());
    }

    // Récupérer le dossier médical d'une personne
    public Optional<MedicalRecord> findMedicalRecordByFullName(String firstName, String lastName) {
        return medicalRecords.stream()
                .filter(mr -> mr.getFirstName().equalsIgnoreCase(firstName) && mr.getLastName().equalsIgnoreCase(lastName))
                .findFirst();
    }
    // Récupérer les adresses desservies par une liste de numéros de caserne
    public List<String> findAddressesByStationNumbers(List<String> stationNumbers) {
        List<String> addresses = fireStations.stream()
                .filter(fs -> stationNumbers.contains(fs.getStation()))
                .map(FireStation::getAddress)
                .collect(Collectors.toList());

        log.info("FireStations loaded: {}", fireStations);
        log.info("Station Numbers: {}", stationNumbers);
        log.info("Addresses found: {}", addresses);

        return addresses;
    }

    public List<Person> findPersonsByAddress(String address) {
        return persons.stream()
                .filter(person -> person.getAddress().equalsIgnoreCase(address))
                .collect(Collectors.toList());
    }

    public Optional<String> findStationNumberByAddress(String address) {
        return fireStations.stream()
                .filter(fs -> fs.getAddress().equalsIgnoreCase(address))
                .map(fs -> String.valueOf(fs.getStation())) // Convertit Integer en String
                .findFirst();
    }

    public List<String> getAddressesByStation(List<Integer> stationNumbers) {
        return fireStations.stream()
                .filter(fs -> stationNumbers.contains(fs.getStation()))  // Vérifie si la station est dans la liste des numéros
                .map(FireStation::getAddress)
                .collect(Collectors.toList());
    }


}
