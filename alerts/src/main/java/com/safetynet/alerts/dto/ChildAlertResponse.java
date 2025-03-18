/*package com.safetynet.alerts.dto;

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
 */
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