package com.example.kafka_interface.producer;

import com.example.kafka_interface.configuration.KafkaConfigProperties;
import com.example.kafka_interface.person.PeopleWrapper;
import com.example.kafka_interface.person.Person;
import org.apache.kafka.clients.producer.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

@Service
public class KafkaProducerService {

    private final Producer<String, String> producer;
    private final ObjectMapper objectMapper;

    // Injecting resource (JSON file) from resources
    @Value("classpath:random-people-data.json")
    private Resource resource;

    @Value("${kafka.topic}")
    private String kafkaTopic;

    private final KafkaConfigProperties kafkaConfigProperties;

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);

    //Constructor that initializes KafkaProducer using injected config properties
    @Autowired
    public KafkaProducerService(KafkaConfigProperties kafkaConfigProperties) {
        this.kafkaConfigProperties = kafkaConfigProperties;

        Properties properties = new Properties();
        properties.put("bootstrap.servers", kafkaConfigProperties.getBootstrapServers());
        properties.put("key.serializer", kafkaConfigProperties.getKeySerializer());
        properties.put("value.serializer", kafkaConfigProperties.getValueSerializer());

        //Idempotent producer so I can leave the write code in main and not worry about duplicates
        properties.put("enable.idempotence", "true");

        producer = new KafkaProducer<>(properties);
        objectMapper = new ObjectMapper();
    }

    //Method to load JSON records into Kafka topic
    public void loadJsonToKafka() throws IOException {
        // Read the JSON file into a list of people
        PeopleWrapper peopleWrapper = objectMapper.readValue(new File(resource.getURI()), PeopleWrapper.class);

        //Get the list of people from the wrapper
        List<Person> people = peopleWrapper.getPeople();
        logger.info("Found {} people", people.size());

        //Send each person as a Kafka message
        for (Person person : people) {
            String personJson = objectMapper.writeValueAsString(person);
            producer.send(new ProducerRecord<>(kafkaTopic, null, personJson), (metadata, exception) -> {
                if (exception != null) {
                    exception.printStackTrace();
                } else {
                    logger.info("Message sent to topic {}", metadata.topic());
                }
            });
        }
    }

    //Close the Kafka producer
    public void close() {
        producer.close();
    }
}