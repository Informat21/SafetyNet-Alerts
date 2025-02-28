package com.safetynet.alerts.service;

import com.safetynet.alerts.repository.DataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhoneAlertService {

    @Autowired
    private DataRepository dataRepository;

    public List<String> getPhoneNumbersByStation(int stationNumber) {
        // Récupérer les adresses couvertes par la caserne
        List<String> addresses = dataRepository.findAllFireStations().stream()
                .filter(fs -> fs.getStation() == stationNumber)
                .map(fs -> fs.getAddress())
                .collect(Collectors.toList());

        // Récupérer les numéros de téléphone des personnes vivant à ces adresses
        return dataRepository.findAll().stream()
                .filter(person -> addresses.contains(person.getAddress()))
                .map(person -> person.getPhone())
                .distinct() // Pour éviter les doublons
                .collect(Collectors.toList());
    }
}
