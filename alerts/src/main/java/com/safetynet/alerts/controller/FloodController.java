package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.FloodStationDTO;
import com.safetynet.alerts.service.FloodService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/flood")
@RequiredArgsConstructor
public class FloodController {

    @Autowired
    private FloodService floodService;

    @GetMapping ("/stations")
    public List<FloodStationDTO> getHouseholdsByStations(@RequestParam List<String> stations) {
        return floodService.getHouseholdsByStations(stations);
    }
}
