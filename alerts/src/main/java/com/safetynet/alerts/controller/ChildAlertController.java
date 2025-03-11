package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.ChildAlertResponse;
import com.safetynet.alerts.service.ChildAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/childAlert")
public class ChildAlertController {

    @Autowired
    private ChildAlertService childAlertService;

    @GetMapping("/childAlert")
    public ChildAlertResponse getChildrenAlert(@RequestParam String address) {
        return childAlertService.getChildrenByAddress(address);
    }
}
