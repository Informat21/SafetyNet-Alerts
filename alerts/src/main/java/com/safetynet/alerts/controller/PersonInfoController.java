package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.PersonInfoResponse;
import com.safetynet.alerts.service.PersonInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/personInfo")
public class PersonInfoController {

    @Autowired
    private PersonInfoService personInfoService;

    @GetMapping
    public ResponseEntity<List<PersonInfoResponse>> getPersonInfo(
            @RequestParam String firstName,
            @RequestParam String lastName) {
        List<PersonInfoResponse> response = personInfoService.getPersonInfo(firstName, lastName);
        if (response.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }
}
