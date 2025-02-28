package com.safetynet.alerts.dto;

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
