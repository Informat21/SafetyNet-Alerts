package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.ChildAlertResponse;
import com.safetynet.alerts.dto.PersonInfoDTO;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.DataRepository;
import com.safetynet.alerts.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChildAlertService {

    @Autowired
    private DataRepository dataRepository;

    public ChildAlertResponse getChildrenByAddress(String address) {
        // Récupérer toutes les personnes à l'adresse donnée
        List<Person> personsAtAddress = dataRepository.findPersonsByAddress(address);

        // Filtrer les enfants (moins de 18 ans)
        List<PersonInfoDTO> children = personsAtAddress.stream()
                .map(person -> {
                    int age = dataRepository.findMedicalRecordByFullName(person.getFirstName(), person.getLastName())
                            .map(medicalRecord -> DateUtils.calculateAge(medicalRecord.getBirthdate()))
                            .orElse(0);
                    return new PersonInfoDTO(person.getFirstName(), person.getLastName(), age);
                })
                .filter(personInfo -> personInfo.getAge() <= 18)
                .collect(Collectors.toList());

        // Récupérer les autres membres du foyer (toutes les personnes à cette adresse sauf les enfants)
        List<PersonInfoDTO> otherHouseholdMembers = personsAtAddress.stream()
                .map(person -> {
                    int age = dataRepository.findMedicalRecordByFullName(person.getFirstName(), person.getLastName())
                            .map(medicalRecord -> DateUtils.calculateAge(medicalRecord.getBirthdate()))
                            .orElse(0);
                    return new PersonInfoDTO(person.getFirstName(), person.getLastName(), age);
                })
                .filter(personInfo -> personInfo.getAge() > 18)
                .collect(Collectors.toList());

        // Retourner la réponse avec les enfants et les autres membres du foyer
        return new ChildAlertResponse(children, otherHouseholdMembers);
    }
}
