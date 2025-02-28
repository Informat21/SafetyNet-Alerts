package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.ChildAlertResponse;
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
public class ChildAlertService {

    @Autowired
    private DataRepository dataRepository;

    public List<ChildAlertResponse> getChildrenAtAddress(String address) {
        List<Person> residents = dataRepository.findAll().stream()
                .filter(person -> person.getAddress().equalsIgnoreCase(address))
                .toList();

        return residents.stream()
                .map(person -> {
                    MedicalRecord medicalRecord = dataRepository.getMedicalRecords().stream()
                            .filter(record -> record.getFirstName().equalsIgnoreCase(person.getFirstName()) &&
                                    record.getLastName().equalsIgnoreCase(person.getLastName()))
                            .findFirst().orElse(null);

                    if (medicalRecord != null) {
                        int age = calculateAge(medicalRecord.getBirthdate());
                        if (age < 18) {
                            List<String> otherMembers = residents.stream()
                                    .filter(p -> !(p.getFirstName().equalsIgnoreCase(person.getFirstName()) &&
                                            p.getLastName().equalsIgnoreCase(person.getLastName())))
                                    .map(p -> p.getFirstName() + " " + p.getLastName())
                                    .collect(Collectors.toList());

                            ChildAlertResponse child = new ChildAlertResponse();
                            child.setFirstName(person.getFirstName());
                            child.setLastName(person.getLastName());
                            child.setAge(age);
                            child.setOtherHouseholdMembers(otherMembers);
                            return child;
                        }
                    }
                    return null;
                })
                .filter(child -> child != null)
                .collect(Collectors.toList());
    }

    private int calculateAge(String birthdate) {
        LocalDate birthDate = LocalDate.parse(birthdate);
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
}
