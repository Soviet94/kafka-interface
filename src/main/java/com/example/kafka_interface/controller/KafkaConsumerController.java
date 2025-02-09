package com.example.kafka_interface.controller;

import com.example.kafka_interface.consumer.KafkaConsumerService;
import com.example.kafka_interface.person.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/topic")
public class KafkaConsumerController {

    private final KafkaConsumerService kafkaConsumerService;

    @Autowired
    public KafkaConsumerController(KafkaConsumerService kafkaConsumerService) {
        this.kafkaConsumerService = kafkaConsumerService;
    }

    // GET endpoint to consume N messages from the specified Kafka topic, starting from the provided offset
    @GetMapping("/{topicName}/{offset}")
    public ResponseEntity<List<String>> consumeFromTopic(
            @PathVariable String topicName,
            @PathVariable long offset,
            @RequestParam(defaultValue = "5") int count) {

        try {
            // Consume messages starting from the provided offset
            List<String> messages = kafkaConsumerService.consumeFromTopic(topicName, offset, count);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            // Handle errors (e.g., wrong topic, offset, etc.)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of("Error consuming messages: " + e.getMessage()));
        }
    }
}