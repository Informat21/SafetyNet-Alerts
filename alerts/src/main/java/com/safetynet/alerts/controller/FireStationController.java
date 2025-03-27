package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.ChildAlertResponse;
import com.safetynet.alerts.dto.FireStationCoverageResponse;
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

    @GetMapping
    public ResponseEntity<List<FireStation>> getAllFireStations() {
        return ResponseEntity.ok(fireStationService.getAllFireStations());
    }

    @PostMapping
    public ResponseEntity<String> addFirestation(@RequestBody FireStation firestation) {
        fireStationService.addFireStation(firestation);
        return ResponseEntity.ok("Mapping caserne/adresse ajouté avec succès.");
    }

    @PutMapping
    public ResponseEntity<String> updateFirestation(@RequestBody FireStation firestation) {
        fireStationService.updateFireStation(firestation);
        return ResponseEntity.ok("Numéro de caserne mis à jour avec succès.");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteFirestation(@RequestParam String address) {
        fireStationService.deleteFireStation(address);
        return ResponseEntity.ok("Mapping caserne/adresse supprimé avec succès.");
    }
    @GetMapping(params = "stationNumber")
    public ResponseEntity<FireStationCoverageResponse> getPersonsCoveredByStation(
            @RequestParam int stationNumber) {
        FireStationCoverageResponse response = fireStationService.getPersonsCoveredByStation(stationNumber);
        if (response == null) { // Vérifie si l'objet retourné est null
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);}

    @GetMapping(value = "/childAlert", params = "address")
    public ResponseEntity<ChildAlertResponse> getChildrenAtAddress(@RequestParam String address) {
        ChildAlertResponse response = fireStationService.getChildrenAtAddress(address);

        if (response == null || response.getChildren() == null || response.getChildren().isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(response);
    }
}
