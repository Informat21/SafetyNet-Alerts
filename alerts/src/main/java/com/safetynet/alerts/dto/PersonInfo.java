package com.safetynet.alerts.dto;

import lombok.Data;

@Data
public class PersonInfo {
    private String firstName;
    private String lastName;
    private String address;
    private String phone;

    public PersonInfo(String firstName, String lastName, String address, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
    }
}
