package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.FloodResponse;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.repository.DataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FloodService {

    @Autowired
    private DataRepository dataRepository;

    public List<FloodResponse> getHouseholdsByStations(List<Integer> stations) {
        List<String> addresses = dataRepository.findAllFireStations().stream()
                .filter(fs -> stations.contains(fs.getStation()))
                .map(FireStation::getAddress)
                .toList();

        Map<String, List<Person>> households = dataRepository.findAll().stream()
                .filter(person -> addresses.contains(person.getAddress()))
                .collect(Collectors.groupingBy(Person::getAddress));

        return households.entrySet().stream().map(entry -> {
            FloodResponse floodResponse = new FloodResponse();
            floodResponse.setAddress(entry.getKey());
            List<FloodResponse.ResidentDetails> residents = entry.getValue().stream().map(person -> {
                MedicalRecord medicalRecord = dataRepository.getMedicalRecords().stream()
                        .filter(record -> record.getFirstName().equalsIgnoreCase(person.getFirstName()) &&
                                record.getLastName().equalsIgnoreCase(person.getLastName()))
                        .findFirst().orElse(null);

                FloodResponse.ResidentDetails details = new FloodResponse.ResidentDetails();
                details.setFirstName(person.getFirstName());
                details.setLastName(person.getLastName());
                details.setPhone(person.getPhone());

                if (medicalRecord != null) {
                    details.setAge(calculateAge(medicalRecord.getBirthdate()));
                    details.setMedications(medicalRecord.getMedications());
                    details.setAllergies(medicalRecord.getAllergies());
                } else {
                    // Gérer le cas où il n'y a pas de dossier médical (optionnel)
                    details.setAge(0); // ou valeur par défaut
                    details.setMedications(Arrays.asList());
                    details.setAllergies(Arrays.asList());
                }
                return details;
            }).collect(Collectors.toList());
            floodResponse.setResidents(residents);
            return floodResponse;
        }).collect(Collectors.toList());
    }

    private int calculateAge(String birthdate) {
        LocalDate birthDate = LocalDate.parse(birthdate);
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
}
