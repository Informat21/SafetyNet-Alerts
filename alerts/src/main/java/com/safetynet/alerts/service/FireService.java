package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.FireResponseDTO;
import com.safetynet.alerts.dto.ResidentInfoDTO;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.DataRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FireService {

    private final DataRepository dataRepository;

    public FireService(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public FireResponseDTO getResidentsByAddress(String address) {
        List<Person> residents = dataRepository.findPersonsByAddress(address);
        Optional<String> stationNumber = dataRepository.findStationNumberByAddress(address);

        if (residents.isEmpty() || stationNumber.isEmpty()) {
            return null; // Aucun habitant ou caserne non trouvée
        }

        List<ResidentInfoDTO> residentDTOs = residents.stream()
                .map(person -> {
                    Optional<MedicalRecord> medicalRecord = dataRepository.findMedicalRecordByFullName(person.getFirstName(), person.getLastName());
                    int age = medicalRecord.map(this::calculateAge).orElse(-1);
                    return new ResidentInfoDTO(
                            person.getFirstName(),
                            person.getLastName(),
                            person.getPhone(),
                            age,
                            medicalRecord.map(MedicalRecord::getMedications).orElse(List.of()),
                            medicalRecord.map(MedicalRecord::getAllergies).orElse(List.of())
                    );
                })
                .collect(Collectors.toList());

        return new FireResponseDTO(stationNumber.get(), residentDTOs);
    }

    private int calculateAge(MedicalRecord medicalRecord) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate birthDate = LocalDate.parse(medicalRecord.getBirthdate(), formatter);
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
}
