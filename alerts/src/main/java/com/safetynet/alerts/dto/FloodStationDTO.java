package com.safetynet.alerts.dto;

import lombok.Data;
import java.util.List;

@Data
public class FloodStationDTO {
    private String address;
    private List<ResidentInfoDTO> residents;
}