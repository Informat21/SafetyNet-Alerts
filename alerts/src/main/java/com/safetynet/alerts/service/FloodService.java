package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.FloodStationDTO;
import com.safetynet.alerts.dto.ResidentInfoDTO;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.repository.DataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FloodService {

    private final DataRepository dataRepository;

    public List<FloodStationDTO> getHouseholdsByStations(List<String> stationNumbers) {
        // Récupérer les adresses desservies par les casernes demandées
        List<String> addresses = dataRepository.findAddressesByStationNumbers(stationNumbers);

        // Grouper les personnes par adresse
        Map<String, List<Person>> personsByAddress = dataRepository.findAll().stream()
                .filter(person -> addresses.contains(person.getAddress()))
                .collect(Collectors.groupingBy(Person::getAddress));

        List<FloodStationDTO> result = new ArrayList<>();

        // Construire la liste de foyers avec détails des habitants
        personsByAddress.forEach((address, persons) -> {
            FloodStationDTO dto = new FloodStationDTO();
            dto.setAddress(address);

            List<ResidentInfoDTO> residents = persons.stream().map(person -> {
                ResidentInfoDTO residentInfo = new ResidentInfoDTO();
                residentInfo.setFirstName(person.getFirstName());
                residentInfo.setLastName(person.getLastName());
                residentInfo.setPhone(person.getPhone());

                // Trouver le dossier médical de la personne
                MedicalRecord medicalRecord = dataRepository
                        .findMedicalRecordByFullName(person.getFirstName(), person.getLastName())
                        .orElse(null);

                if (medicalRecord != null) {
                    // Calculer l'âge
                    residentInfo.setAge(calculateAge(medicalRecord.getBirthdate()));
                    residentInfo.setMedications(medicalRecord.getMedications());
                    residentInfo.setAllergies(medicalRecord.getAllergies());
                }
                return residentInfo;
            }).collect(Collectors.toList());

            dto.setResidents(residents);
            result.add(dto);
        });

        return result;
    }

    private int calculateAge(String birthdate) {
        LocalDate birthDate = LocalDate.parse(birthdate);
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
}
