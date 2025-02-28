package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.ChildAlertResponse;
import com.safetynet.alerts.dto.FireStationCoverageResponse;
import com.safetynet.alerts.dto.PersonInfo;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.DataRepository;
import com.safetynet.alerts.service.FireStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

@Service
public class FireStationServiceImpl implements FireStationService {

    @Autowired
    private DataRepository dataRepository;

    @Override
    public List<FireStation> getAllFireStations() {
        return dataRepository.findAllFireStations();
    }

    @Override
    public void addFireStation(FireStation fireStation) {
        dataRepository.saveFireStation(fireStation);
    }

    @Override
    public void updateFireStation(FireStation fireStation) {
        dataRepository.updateFireStation(fireStation);
    }

    @Override
    public void deleteFireStation(String address) {
        dataRepository.deleteFireStationByAddress(address);
    }

    @Override
    public Optional<FireStation> getFireStationByAddress(String address) {
        return dataRepository.findAllFireStations().stream()
                .filter(fs -> fs.getAddress().equalsIgnoreCase(address))
                .findFirst();
    }

    @Override
    public FireStationCoverageResponse getPersonsCoveredByStation(int stationNumber) {
        // Récupérer les adresses couvertes par la station
        List<String> coveredAddresses = dataRepository.findAllFireStations().stream()
                .filter(fs -> fs.getStation() == stationNumber)
                .map(FireStation::getAddress)
                .toList();

        // Récupérer les personnes vivant à ces adresses
        List<Person> coveredPersons = dataRepository.findAll().stream()
                .filter(person -> coveredAddresses.contains(person.getAddress()))
                .toList();

        // Vérification si la liste des personnes couvertes est vide
        if (coveredPersons.isEmpty()) {
            return new FireStationCoverageResponse(emptyList(), 0, 0);
        }

        // Construire la réponse
        List<PersonInfo> personInfos = coveredPersons.stream()
                .map(person -> new PersonInfo(person.getFirstName(), person.getLastName(), person.getAddress(), person.getPhone()))
                .collect(Collectors.toList());

        // Compter les adultes et les enfants
        int adultCount = 0;
        int childCount = 0;
        for (Person person : coveredPersons) {
            Optional<MedicalRecord> medicalRecord = dataRepository.getMedicalRecords().stream()
                    .filter(record -> record.getFirstName().equalsIgnoreCase(person.getFirstName()) &&
                            record.getLastName().equalsIgnoreCase(person.getLastName()))
                    .findFirst();
            if (medicalRecord.isPresent()) {
                int age = calculateAge(medicalRecord.get().getBirthdate());
                if (age > 18) {
                    adultCount++;
                } else {
                    childCount++;
                }
            }

        }

        return new FireStationCoverageResponse(personInfos, adultCount, childCount);
    }

    private int calculateAge(String birthdate) {
        LocalDate birthDate = LocalDate.parse(birthdate);
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    @Override
    public ChildAlertResponse getChildrenAtAddress(String address) {
        // Récupérer toutes les personnes vivant à l'adresse donnée
        List<Person> residents = dataRepository.findAll().stream()
                .filter(person -> person.getAddress().equalsIgnoreCase(address))
                .collect(Collectors.toList());

        // Séparer les enfants (âge ≤ 18) des autres membres du foyer
        List<PersonInfo> children = new ArrayList<>();
        List<PersonInfo> otherMembers = new ArrayList<>();

        for (Person person : residents) {
            Optional<MedicalRecord> medicalRecord = dataRepository.getMedicalRecords().stream()
                    .filter(record -> record.getFirstName().equalsIgnoreCase(person.getFirstName()) &&
                            record.getLastName().equalsIgnoreCase(person.getLastName()))
                    .findFirst();

            if (medicalRecord.isPresent()) {
                int age = calculateAge(medicalRecord.get().getBirthdate());
                PersonInfo personInfo = new PersonInfo(person.getFirstName(), person.getLastName(), person.getAddress(), person.getPhone());

                if (age <= 18) {
                    children.add(personInfo);
                } else {
                    otherMembers.add(personInfo);
                }
            }
        }

        // Retourne la liste des enfants et des autres membres du foyer
        return new ChildAlertResponse(children, otherMembers);
    }

}