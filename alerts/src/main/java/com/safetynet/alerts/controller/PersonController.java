package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping
    public List<Person> getAllPersons() {
        return personService.findAll();
    }

    @PostMapping
    public ResponseEntity<String> addPerson(@RequestBody Person person) {
        personService.save(person);
        return ResponseEntity.status(HttpStatus.CREATED).body("Person added successfully");
    }

    @PutMapping
    public ResponseEntity<String> updatePerson(@RequestBody Person person) {
        Optional<Person> existingPerson = personService.findByFullName(person.getFirstName(), person.getLastName());
        if (existingPerson.isPresent()) {
            personService.update(person);
            return ResponseEntity.ok("Person updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person not found");
        }
    }

    @DeleteMapping
    public ResponseEntity<String> deletePerson(@RequestParam String firstName, @RequestParam String lastName) {
        Optional<Person> existingPerson = personService.findByFullName(firstName, lastName);
        if (existingPerson.isPresent()) {
            personService.delete(firstName, lastName);
            return ResponseEntity.ok("Person deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person not found");
        }
    }
}
