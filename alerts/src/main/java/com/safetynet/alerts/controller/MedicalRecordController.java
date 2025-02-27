package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/medicalRecord")
public class MedicalRecordController {

    @Autowired
    private MedicalRecordService medicalRecordService;

    // Récupérer tous les dossiers médicaux
    @GetMapping
    public List<MedicalRecord> getAllMedicalRecords() {
        return medicalRecordService.getAllMedicalRecords();
    }

    // Ajouter un dossier médical
    @PostMapping
    public ResponseEntity<String> addMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        medicalRecordService.addMedicalRecord(medicalRecord);
        return new ResponseEntity<>("Dossier médical ajouté avec succès.", HttpStatus.CREATED);
    }

    // Mettre à jour un dossier médical existant
    @PutMapping
    public ResponseEntity<String> updateMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        Optional<MedicalRecord> existingRecord = medicalRecordService.getMedicalRecordByFullName(
                medicalRecord.getFirstName(), medicalRecord.getLastName());

        if (existingRecord.isPresent()) {
            medicalRecordService.updateMedicalRecord(medicalRecord);
            return new ResponseEntity<>("Dossier médical mis à jour avec succès.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Dossier médical non trouvé.", HttpStatus.NOT_FOUND);
        }
    }

    // Supprimer un dossier médical
    @DeleteMapping
    public ResponseEntity<String> deleteMedicalRecord(
            @RequestParam String firstName, @RequestParam String lastName) {
        Optional<MedicalRecord> existingRecord = medicalRecordService.getMedicalRecordByFullName(firstName, lastName);

        if (existingRecord.isPresent()) {
            medicalRecordService.deleteMedicalRecord(firstName, lastName);
            return new ResponseEntity<>("Dossier médical supprimé avec succès.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Dossier médical non trouvé.", HttpStatus.NOT_FOUND);
        }
    }
}