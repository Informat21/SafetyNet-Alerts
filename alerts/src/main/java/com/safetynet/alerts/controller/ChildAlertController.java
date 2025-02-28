package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.ChildAlertResponse;
import com.safetynet.alerts.service.ChildAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/childAlert")
public class ChildAlertController {

    @Autowired
    private ChildAlertService childAlertService;

    @GetMapping
    public ResponseEntity<List<ChildAlertResponse>> getChildrenAtAddress(@RequestParam String address) {
        List<ChildAlertResponse> children = childAlertService.getChildrenAtAddress(address);
        if (children.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(children);
    }
}
