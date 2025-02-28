package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.FloodResponse;
import com.safetynet.alerts.service.FloodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/flood/stations")
public class FloodController {

    @Autowired
    private FloodService floodService;

    @GetMapping
    public ResponseEntity<List<FloodResponse>> getHouseholdsByStations(@RequestParam List<Integer> stations) {
        List<FloodResponse> response = floodService.getHouseholdsByStations(stations);
        if (response.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }
}
