package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Person;

import java.util.List;
import java.util.Optional;

public interface PersonService {
    List<Person> findAll();
    Optional<Person> findByFullName(String firstName, String lastName);
    void save(Person person);
    void update(Person person);
    void delete(String firstName, String lastName);
}
