package com.safetynet.alerts.controller;

import com.safetynet.alerts.service.CommunityEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/communityEmail")
public class CommunityEmailController {

    @Autowired
    private CommunityEmailService communityEmailService;

    @GetMapping
    public ResponseEntity<List<String>> getEmailsByCity(@RequestParam String city) {
        List<String> emails = communityEmailService.getEmailsByCity(city);
        if (emails.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(emails);
    }
}
