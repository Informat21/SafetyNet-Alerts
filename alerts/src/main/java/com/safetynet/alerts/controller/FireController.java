package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.FireResponseDTO;
import com.safetynet.alerts.service.FireService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FireController {

    private final FireService fireService;

    public FireController(FireService fireService) {
        this.fireService = fireService;
    }

    @GetMapping("/fire")
    public ResponseEntity<FireResponseDTO> getResidentsByAddress(@RequestParam String address) {
        FireResponseDTO response = fireService.getResidentsByAddress(address);

        if (response == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(response);
    }
}
