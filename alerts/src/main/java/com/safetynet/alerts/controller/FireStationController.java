package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.service.FireStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/firestation")
public class FireStationController {

    @Autowired
    private FireStationService fireStationService;

    // Récupérer tous les mappings caserne/adresse
    @GetMapping
    public List<FireStation> getAllFireStations() {
        return fireStationService.getAllFireStations();
    }

    // Ajouter un nouveau mapping caserne/adresse
    @PostMapping
    public ResponseEntity<String> addFireStation(@RequestBody FireStation fireStation) {
        fireStationService.addFireStation(fireStation);
        return ResponseEntity.ok("Mapping caserne/adresse ajouté avec succès.");
    }

    // Mettre à jour le numéro de caserne d'une adresse
    @PutMapping
    public ResponseEntity<String> updateFireStation(@RequestBody FireStation fireStation) {
        fireStationService.updateFireStation(fireStation);
        return ResponseEntity.ok("Numéro de caserne mis à jour avec succès.");
    }

    // Supprimer un mapping caserne/adresse
    @DeleteMapping
    public ResponseEntity<String> deleteFireStation(@RequestParam String address) {
        fireStationService.deleteFireStation(address);
        return ResponseEntity.ok("Mapping caserne/adresse supprimé avec succès.");
    }
}
