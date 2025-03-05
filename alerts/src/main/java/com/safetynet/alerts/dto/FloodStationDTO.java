/*package com.safetynet.alerts.dto;

import lombok.Data;
import java.util.List;

@Data
public class FloodStationDTO {
    private String address;
    private List<ResidentInfo> residents;

    @Data
    public static class ResidentInfo {
        private String firstName;
        private String lastName;
        private String phone;
        private int age;
        private List<String> medications;
        private List<String> allergies;
    }
}

 */
package com.safetynet.alerts.dto;

import lombok.Data;
import java.util.List;

@Data
public class FloodStationDTO {
    private String address;
    private List<ResidentInfoDTO> residents;
}