package com.safetynet.alerts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonInfoDTO {
    private String firstName;
    private String lastName;
    private String address;
    private int age;
    private String phone;
    private String email;
    private List<String> medications;
    private List<String> allergies;

    //  Ajout d'un constructeur simplifié pour ChildAlert
    public PersonInfoDTO(String firstName, String lastName, int age){
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }
}