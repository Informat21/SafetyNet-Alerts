package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.PersonInfoResponse;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.repository.DataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonInfoService {

    @Autowired
    private DataRepository dataRepository;

    public List<PersonInfoResponse> getPersonInfo(String firstName, String lastName) {
        List<Person> persons = dataRepository.findAll().stream()
                .filter(person -> person.getLastName().equalsIgnoreCase(lastName) &&
                        person.getFirstName().equalsIgnoreCase(firstName))
                .collect(Collectors.toList());

        return persons.stream().map(person -> {
            MedicalRecord medicalRecord = dataRepository.getMedicalRecords().stream()
                    .filter(record -> record.getFirstName().equalsIgnoreCase(person.getFirstName()) &&
                            record.getLastName().equalsIgnoreCase(person.getLastName()))
                    .findFirst().orElse(null);

            PersonInfoResponse response = new PersonInfoResponse();
            response.setFirstName(person.getFirstName());
            response.setLastName(person.getLastName());
            response.setAddress(person.getAddress());
            response.setEmail(person.getEmail());

            if (medicalRecord != null) {
                response.setAge(calculateAge(medicalRecord.getBirthdate()));
                response.setMedications(medicalRecord.getMedications());
                response.setAllergies(medicalRecord.getAllergies());
            }
            return response;
        }).collect(Collectors.toList());
    }

    private int calculateAge(String birthdate) {
        LocalDate birthDate = LocalDate.parse(birthdate);
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
}
