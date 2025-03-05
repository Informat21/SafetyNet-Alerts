package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.PersonInfoDTO;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.repository.DataRepository;
import com.safetynet.alerts.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonInfoService {

    @Autowired
    private DataRepository dataRepository;

    public List<PersonInfoDTO> getPersonInfoByLastName(String lastName) {
        List<Person> persons = dataRepository.findByLastName(lastName);

        return persons.stream().map(person -> {
            PersonInfoDTO dto = new PersonInfoDTO();
            dto.setFirstName(person.getFirstName());
            dto.setLastName(person.getLastName());
            dto.setAddress(person.getAddress());
            dto.setEmail(person.getEmail());

            // Trouver le dossier médical associé
            dataRepository.findMedicalRecordByFullName(person.getFirstName(), person.getLastName())
                    .ifPresent(medicalRecord -> {
                        dto.setMedications(medicalRecord.getMedications());
                        dto.setAllergies(medicalRecord.getAllergies());
                        dto.setAge(DateUtils.calculateAge(medicalRecord.getBirthdate()));
                    });

            return dto;
        }).collect(Collectors.toList());
    }
}
