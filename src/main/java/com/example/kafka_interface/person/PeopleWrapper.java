package com.example.kafka_interface.person;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class PeopleWrapper {

    @JsonProperty("ctRoot")
    private List<Person> people;

    public List<Person> getPeople() {
        return people;
    }

    public void setPeople(List<Person> people) {
        this.people = people;
    }
}