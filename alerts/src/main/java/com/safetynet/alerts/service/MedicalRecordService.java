package com.safetynet.alerts.service;

import com.safetynet.alerts.model.MedicalRecord;
import java.util.List;
import java.util.Optional;

public interface MedicalRecordService {

    List<MedicalRecord> getAllMedicalRecords();
    void addMedicalRecord(MedicalRecord medicalRecord);
    void updateMedicalRecord(MedicalRecord medicalRecord);
    void deleteMedicalRecord(String firstName, String lastName);
    Optional<MedicalRecord> getMedicalRecordByFullName(String firstName, String lastName);
}
