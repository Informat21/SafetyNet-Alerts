package com.safetynet.alerts.dto;

import lombok.Data;
import java.util.List;

@Data
public class ChildAlertResponse {
    private String firstName;
    private String lastName;
    private int age;
    private List<String> otherHouseholdMembers;

    public ChildAlertResponse(List<PersonInfo> children, List<PersonInfo> otherMembers) {
    }

    public ChildAlertResponse() {
        
    }

    public CharSequence getChildren() {
        return null;
    }
}
