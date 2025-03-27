package com.safetynet.alerts.service;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.repository.DataRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {

    @Autowired
    private DataRepository dataRepository;

    @Override
    public List<MedicalRecord> getAllMedicalRecords() {
        return dataRepository.getMedicalRecords();
    }

    @Override
    public void addMedicalRecord(MedicalRecord medicalRecord) {
        dataRepository.getMedicalRecords().add(medicalRecord);
    }

    @Override
    public void updateMedicalRecord(MedicalRecord updatedRecord) {
        dataRepository.getMedicalRecords().stream()
                .filter(mr -> mr.getFirstName().equalsIgnoreCase(updatedRecord.getFirstName()) &&
                        mr.getLastName().equalsIgnoreCase(updatedRecord.getLastName()))
                .findFirst()
                .ifPresent(medicalRecord -> {
                    medicalRecord.setBirthdate(updatedRecord.getBirthdate());
                    medicalRecord.setMedications(updatedRecord.getMedications());
                    medicalRecord.setAllergies(updatedRecord.getAllergies());
                });
    }

    @Override
    public void deleteMedicalRecord(String firstName, String lastName) {
        dataRepository.getMedicalRecords().removeIf(mr ->
                mr.getFirstName().equalsIgnoreCase(firstName) &&
                        mr.getLastName().equalsIgnoreCase(lastName));
    }

    @Override
    public Optional<MedicalRecord> getMedicalRecordByFullName(String firstName, String lastName) {
        return dataRepository.getMedicalRecords().stream()
                .filter(mr -> mr.getFirstName().equalsIgnoreCase(firstName) &&
                        mr.getLastName().equalsIgnoreCase(lastName))
                .findFirst();
    }
}