package com.safetynet.alerts.dto;

import lombok.Data;
import java.util.List;

@Data
public class FireResponse {
    private String firstName;
    private String lastName;
    private int age;
    private String phone;
    private List<String> medications;
    private List<String> allergies;
    private int stationNumber;
}
