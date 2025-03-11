/*package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.ChildAlertResponse;
import com.safetynet.alerts.dto.PersonInfoDTO;
import com.safetynet.alerts.service.ChildAlertService;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.DataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChildAlertController.class) // Annotations pour indiquer que c'est un test de contrôleur
public class ChildAlertControllerTest {

    private MockMvc mockMvc; // MockMvc permet de simuler les requêtes HTTP

    @Mock
    private ChildAlertService childAlertService; // Service de récupération des alertes enfants, ici simulé avec @Mock

    @InjectMocks
    private ChildAlertController childAlertController; // Injecte les mocks dans le contrôleur

    @Mock
    private DataRepository dataRepository; // Le repository de données, simulé ici

    @BeforeEach
    public void setup() {
        // Initialise les mocks et le contrôleur avant chaque test
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(childAlertController).build(); // Configuration de MockMvc pour tester le contrôleur
    }

    @Test
    public void testGetChildrenAlert_withChildren() throws Exception {
        // Données simulées
        String address = "123 Main St"; // Adresse test
        List<Person> persons = Arrays.asList(
                new Person("John", "Doe", address, "City", "12345", "555-1234", "john.doe@example.com", 1),
                new Person("Jane", "Doe", address, "City", "12345", "555-5678", "jane.doe@example.com", 1)
        );

        // Réponse attendue simulée
        ChildAlertResponse response = new ChildAlertResponse(
                persons.stream().map(p -> new PersonInfoDTO(p.getFirstName(), p.getLastName(), 10)).collect(Collectors.toList()),
                Arrays.asList() // Pas d'adultes dans cet exemple
        );

        // Quand le service est appelé, on retourne la réponse simulée
        when(childAlertService.getChildrenByAddress(address)).thenReturn(response);

        // Exécution de la requête HTTP
        mockMvc.perform(get("/childAlert").param("address", address)) // Envoie d'une requête GET avec un paramètre d'adresse
                .andExpect(status().isOk()) // Vérifie que le statut HTTP de la réponse est 200 (OK)
                .andExpect(jsonPath("$.children.length()").value(2)) // Vérifie que la réponse contient 2 enfants
                .andExpect(jsonPath("$.children[0].firstName").value("John")) // Vérifie le prénom du premier enfant
                .andExpect(jsonPath("$.children[0].lastName").value("Doe")) // Vérifie le nom de famille du premier enfant
                .andExpect(jsonPath("$.children[0].age").value(10)); // Vérifie l'âge du premier enfant
    }

    @Test
    public void testGetChildrenAlert_withNoChildren() throws Exception {
        // Données simulées pour une adresse sans enfant
        String address = "456 Elm St";
        List<Person> persons = List.of(
                new Person("Alice", "Smith", address, "City", "12345", "555-2345", "alice.smith@example.com", 1)
        );

        // Réponse simulée, ici pas d'enfants et donc une liste vide
        ChildAlertResponse response = new ChildAlertResponse(
                List.of(), // Pas d'enfants
                List.of() // Pas d'adultes
        );

        // Quand le service est appelé, on retourne la réponse simulée
        when(childAlertService.getChildrenByAddress(address)).thenReturn(response);

        // Exécution de la requête HTTP
        mockMvc.perform(get("/childAlert").param("address", address)) // Envoie d'une requête GET avec un paramètre d'adresse
                .andExpect(status().isOk()) // Vérifie que le statut HTTP de la réponse est 200 (OK)
                .andExpect(content().string(""));  // Vérifie que la réponse est une chaîne vide (pas d'enfants)
    }
}
*/
package com.safetynet.alerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.dto.ChildAlertResponse;
import com.safetynet.alerts.dto.PersonInfoDTO;
import com.safetynet.alerts.service.FireStationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ChildAlertController.class)
class ChildAlertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FireStationService fireStationService;

    @InjectMocks
    private FireStationController fireStationController;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetChildrenAtAddress_ShouldReturnChildrenWithOtherHouseholdMembers() throws Exception {
        // Mock des données : Un enfant et un adulte au même domicile
        PersonInfoDTO child = new PersonInfoDTO("Alice", "Brown", "10 Downing St", 8, "123-456-7890", "", Collections.emptyList(), Collections.emptyList());
        PersonInfoDTO adult = new PersonInfoDTO("Bob", "Brown", "10 Downing St", 35, "123-456-7891", "", Collections.emptyList(), Collections.emptyList());

        ChildAlertResponse response = new ChildAlertResponse(Collections.singletonList(child), Collections.singletonList(adult));

        when(fireStationService.getChildrenAtAddress("10 Downing St")).thenReturn(response);

        mockMvc.perform(get("/childAlert?address=10 Downing St"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.children[0].firstName").value("Alice"))
                .andExpect(jsonPath("$.children[0].lastName").value("Brown"))
                .andExpect(jsonPath("$.children[0].age").value(8))
                .andExpect(jsonPath("$.otherMembers[0].firstName").value("Bob"))
                .andExpect(jsonPath("$.otherMembers[0].lastName").value("Brown"));
    }

    @Test
    void testGetChildrenAtAddress_NoChildrenFound_ShouldReturnEmptyList() throws Exception {
        ChildAlertResponse response = new ChildAlertResponse(Collections.emptyList(), Arrays.asList(
                new PersonInfoDTO("Bob", "Brown", "10 Downing St", 35, "123-456-7891", "", Collections.emptyList(), Collections.emptyList())
        ));

        when(fireStationService.getChildrenAtAddress("10 Downing St")).thenReturn(response);

        mockMvc.perform(get("/childAlert?address=10 Downing St"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.children").isEmpty())
                .andExpect(jsonPath("$.otherMembers[0].firstName").value("Bob"));
    }

    @Test
    void testGetChildrenAtAddress_AddressDoesNotExist_ShouldReturnNoContent() throws Exception {
        when(fireStationService.getChildrenAtAddress("Unknown Address")).thenReturn(null);

        mockMvc.perform(get("/childAlert?address=Unknown Address"))
                .andExpect(status().isNoContent());
    }
}
