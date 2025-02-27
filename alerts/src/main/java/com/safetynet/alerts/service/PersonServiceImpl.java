package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.DataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private DataRepository dataRepository;

    @Override
    public List<Person> findAll() {
        return dataRepository.findAll();
    }

    @Override
    public Optional<Person> findByFullName(String firstName, String lastName) {
        return dataRepository.findByFullName(firstName, lastName);
    }

    @Override
    public void save(Person person) {
        dataRepository.save(person);
    }

    @Override
    public void update(Person person) {
        Optional<Person> existingPerson = dataRepository.findByFullName(person.getFirstName(), person.getLastName());
        if (existingPerson.isPresent()) {
            dataRepository.update(person);
        } else {
            throw new RuntimeException("Person not found");
        }
    }

    @Override
    public void delete(String firstName, String lastName) {
        Optional<Person> existingPerson = dataRepository.findByFullName(firstName, lastName);
        if (existingPerson.isPresent()) {
            dataRepository.delete(firstName, lastName);
        } else {
            throw new RuntimeException("Person not found");
        }
    }
}
