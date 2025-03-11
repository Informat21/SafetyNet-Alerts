package com.safetynet.alerts.model;

import com.safetynet.alerts.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalRecord {
    private String firstName;
    private String lastName;
    private String birthdate;
    private List<String> medications;
    private List<String> allergies;

    public int getAge() {
        return DateUtils.calculateAge(birthdate); // Utilisation de la méthode utilitaire
    }
}
