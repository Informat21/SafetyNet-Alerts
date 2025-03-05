package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.PersonInfoDTO;
import com.safetynet.alerts.service.PersonInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping ("/personInfolastName")
public class PersonInfoController {

    @Autowired
    private PersonInfoService personInfoService;

    @GetMapping
    public List<PersonInfoDTO> getPersonInfo(@RequestParam String lastName) {
        return personInfoService.getPersonInfoByLastName(lastName);
    }
}
