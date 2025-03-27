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
}