package com.example.kafka_interface.person;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PersonDeserializer implements Deserializer<Person> {

    private ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger logger = LoggerFactory.getLogger(PersonDeserializer.class);

    @Override
    public Person deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, Person.class);
        } catch (Exception e) {
            logger.error("Error deserializing data from topic {}: {}", topic, e.getMessage());
        }
        return null;
    }

}