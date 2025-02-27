package com.safetynet.alerts.service;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.repository.DataRepository;
import com.safetynet.alerts.service.FireStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FireStationServiceImpl implements FireStationService {

    @Autowired
    private DataRepository dataRepository;

    @Override
    public List<FireStation> getAllFireStations() {
        return dataRepository.findAllFireStations();
    }

    @Override
    public void addFireStation(FireStation fireStation) {
        dataRepository.saveFireStation(fireStation);
    }

    @Override
    public void updateFireStation(FireStation fireStation) {
        dataRepository.updateFireStation(fireStation);
    }

    @Override
    public void deleteFireStation(String address) {
        dataRepository.deleteFireStationByAddress(address);
    }

    @Override
    public Optional<FireStation> getFireStationByAddress(String address) {
        return dataRepository.findAllFireStations().stream()
                .filter(fs -> fs.getAddress().equalsIgnoreCase(address))
                .findFirst();
    }
}