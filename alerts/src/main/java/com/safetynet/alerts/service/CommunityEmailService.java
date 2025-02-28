package com.safetynet.alerts.service;

import com.safetynet.alerts.repository.DataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommunityEmailService {

    @Autowired
    private DataRepository dataRepository;

    public List<String> getEmailsByCity(String city) {
        return dataRepository.findAll().stream()
                .filter(person -> person.getCity().equalsIgnoreCase(city))
                .map(person -> person.getEmail().toLowerCase())
                .distinct()
                .collect(Collectors.toList());
    }
}
