/*package com.safetynet.alerts.dto;

import lombok.Data;
import java.util.List;

@Data
public class FireStationCoverageResponse {
    private List<PersonInfo> persons;
    private int adultCount;
    private int childCount;

    public FireStationCoverageResponse(List<PersonInfo> persons, int adultCount, int childCount) {
        this.persons = persons;
        this.adultCount = adultCount;
        this.childCount = childCount;
    }
}


package com.safetynet.alerts.dto;

import lombok.Data;
import java.util.List;

@Data
public class FireStationCoverageResponse {
    private List<PersonInfoDTO> persons;
    private int adultCount;
    private int childCount;

    public FireStationCoverageResponse(List<PersonInfoDTO> persons, int adultCount, int childCount) {
        this.persons = persons;
        this.adultCount = adultCount;
        this.childCount = childCount;
    }
} */

package com.safetynet.alerts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class FireStationCoverageResponse {
    private List<PersonInfoDTO> persons;
    private int numberOfAdults;
    private int numberOfChildren;

    @Data
    @AllArgsConstructor
    public static class PersonInfo {
        private String firstName;
        private String lastName;
        private String address;
        private String phone;
    }
}