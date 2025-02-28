package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.FireResponse;
import com.safetynet.alerts.service.FireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/fire")
public class FireController {

    @Autowired
    private FireService fireService;

    @GetMapping
    public ResponseEntity<List<FireResponse>> getPersonsByAddress(@RequestParam String address) {
        List<FireResponse> response = fireService.getPersonsByAddress(address);
        if (response.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }
}
