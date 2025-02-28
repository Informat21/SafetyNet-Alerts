package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.FireResponse;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.repository.DataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FireService {

    @Autowired
    private DataRepository dataRepository;

    public List<FireResponse> getPersonsByAddress(String address) {
        // Récupérer le numéro de la caserne pour cette adresse
        FireStation fireStation = dataRepository.findAllFireStations().stream()
                .filter(fs -> fs.getAddress().equalsIgnoreCase(address))
                .findFirst().orElse(null);

        if (fireStation == null) {
            return List.of(); // Aucune caserne trouvée pour cette adresse
        }

        // Récupérer les résidents de cette adresse
        List<Person> residents = dataRepository.findAll().stream()
                .filter(person -> person.getAddress().equalsIgnoreCase(address))
                .toList();

        // Construire la réponse
        return residents.stream().map(person -> {
            MedicalRecord medicalRecord = dataRepository.getMedicalRecords().stream()
                    .filter(record -> record.getFirstName().equalsIgnoreCase(person.getFirstName()) &&
                            record.getLastName().equalsIgnoreCase(person.getLastName()))
                    .findFirst().orElse(null);

            FireResponse fireResponse = new FireResponse();
            fireResponse.setFirstName(person.getFirstName());
            fireResponse.setLastName(person.getLastName());
            fireResponse.setPhone(person.getPhone());
            fireResponse.setStationNumber(fireStation.getStation());

            if (medicalRecord != null) {
                fireResponse.setAge(calculateAge(medicalRecord.getBirthdate()));
                fireResponse.setMedications(medicalRecord.getMedications());
                fireResponse.setAllergies(medicalRecord.getAllergies());
            }
            return fireResponse;
        }).collect(Collectors.toList());
    }

    private int calculateAge(String birthdate) {
        LocalDate birthDate = LocalDate.parse(birthdate);
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
}
