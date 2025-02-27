package com.safetynet.alerts.service;

import com.safetynet.alerts.model.FireStation;
import java.util.List;
import java.util.Optional;

public interface FireStationService {

    List<FireStation> getAllFireStations();
    void addFireStation(FireStation fireStation);
    void updateFireStation(FireStation fireStation);
    void deleteFireStation(String address);
    Optional<FireStation> getFireStationByAddress(String address);
}
