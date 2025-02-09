package com.example.kafka_interface.person;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Address {
    @JsonProperty("street")
    String street;
    @JsonProperty("town")
    String town;
    @JsonProperty("postode")
    String postcode;

    // No-argument constructor
    public Address() {}

    public Address(String street, String town, String postcode){
        this.street = street;
        this.town = town;
        this.postcode = postcode;
    }

}