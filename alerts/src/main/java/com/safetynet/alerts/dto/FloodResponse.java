package com.safetynet.alerts.dto;

import lombok.Data;
import java.util.List;

@Data
public class FloodResponse {
    private String address;
    private List<ResidentDetails> residents;

    @Data
    public static class ResidentDetails {
        private String firstName;
        private String lastName;
        private int age;
        private String phone;
        private List<String> medications;
        private List<String> allergies;
    }
}
