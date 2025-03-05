/*package com.safetynet.alerts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FireResponseDTO {
    private String stationNumber;
    private List<ResidentInfoDTO> residents;
}

 */
package com.safetynet.alerts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class FireResponseDTO {
    private String stationNumber;
    private List<ResidentInfoDTO> residents;
}