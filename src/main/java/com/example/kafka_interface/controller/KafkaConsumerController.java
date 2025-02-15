package com.example.kafka_interface.controller;

import com.example.kafka_interface.consumer.KafkaConsumerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/topic")
public class KafkaConsumerController {

    private final KafkaConsumerService kafkaConsumerService;

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerController.class);

    @Autowired
    public KafkaConsumerController(KafkaConsumerService kafkaConsumerService) {
        this.kafkaConsumerService = kafkaConsumerService;
    }

    //GET endpoint to consume N messages from the specified Kafka topic, starting from the provided offset
    @GetMapping("/{topicName}/{offset}")
    public ResponseEntity<List<String>> consumeFromTopic(
            @PathVariable String topicName,
            @PathVariable long offset,
            @RequestParam(defaultValue = "5") int count) {

        try {
            //Consume messages starting from the provided offset
            List<String> messages = kafkaConsumerService.consumeFromTopic(topicName, offset, count);
            return ResponseEntity.ok(messages);
        } catch (HttpServerErrorException e) {
            logger.error("Error while consuming messages", e);
            //Handle errors (e.g., wrong topic, offset, etc.)
            return ResponseEntity.status(e.getStatusCode()).body(new ArrayList<>());
        }
    }

    //GET endpoint to find a topic
    @GetMapping("/{topicName}")
    public ResponseEntity<Void> findTopic(@PathVariable String topicName) {
        try {
            //Consume messages starting from the provided offset
            boolean found = kafkaConsumerService.findTopic(topicName);

            if (found) {
                return ResponseEntity.status(HttpStatus.FOUND).build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

        } catch (Exception e) {
            logger.error("Error finding topic", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}