package com.example.kafka_interface.person;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class Person {

    @JsonProperty("_id")
    String id;
    @JsonProperty("name")
    String name;
    @JsonProperty("dob")
    String dob;
    @JsonProperty("address")
    Address address;
    @JsonProperty("telephone")
    String telephone;
    @JsonProperty("pets")
    String[] pets;
    @JsonProperty("score")
    float score;
    @JsonProperty("email")
    String email;
    @JsonProperty("url")
    String url;
    @JsonProperty("description")
    String description;
    @JsonProperty("verified")
    boolean verified;
    @JsonProperty("salary")
    int salary;

    public Person() {}

    public Person(String id, String name, String dob, Address address, String telephone, String[] pets, float score, String email, String url, String description, boolean verified, int salary) {
        this.id = id;
        this.name = name;
        this.dob = dob;
        this.address = address;
        this.telephone = telephone;
        this.pets = pets;
        this.score = score;
        this.email = email;
        this.url = url;
        this.description = description;
        this.verified = verified;
        this.salary = salary;
    }

    @Override
    public String toString() {
        try {
            // Create an ObjectMapper instance
            ObjectMapper objectMapper = new ObjectMapper();

            // Optionally, enable pretty-printing of the JSON
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

            // Convert the Person object to a JSON string
            return objectMapper.writeValueAsString(this);
        } catch (Exception e) {
            // Handle any exceptions that might occur during serialization
            e.printStackTrace();
            return "{}";  // Return an empty JSON object if serialization fails
        }
    }
}