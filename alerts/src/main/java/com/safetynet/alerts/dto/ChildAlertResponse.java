
package com.safetynet.alerts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChildAlertResponse {
    private List<PersonInfoDTO> children;
    private List<PersonInfoDTO> otherHouseholdMembers;
}