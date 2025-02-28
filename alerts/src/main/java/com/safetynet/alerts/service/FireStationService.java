package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.ChildAlertResponse;
import com.safetynet.alerts.dto.FireStationCoverageResponse;
import com.safetynet.alerts.model.FireStation;
import java.util.List;
import java.util.Optional;

public interface FireStationService {

    List<FireStation> getAllFireStations();
    void addFireStation(FireStation fireStation);
    void updateFireStation(FireStation fireStation);
    void deleteFireStation(String address);
    Optional<FireStation> getFireStationByAddress(String address);

    // Nouvelle méthode pour récupérer les personnes couvertes par une caserne
    FireStationCoverageResponse getPersonsCoveredByStation(int stationNumber);

    ChildAlertResponse getChildrenAtAddress(String address);


}



