package com.example.kafka_interface.consumer;

import com.example.kafka_interface.configuration.KafkaConfigProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class KafkaConsumerService {

    private final KafkaConsumer<String, String> consumer;
    private final ObjectMapper objectMapper;

    private final KafkaConfigProperties kafkaConfigProperties;

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    //Make into singleton?
    @Autowired
    public KafkaConsumerService(KafkaConfigProperties kafkaConfigProperties) {
        this.kafkaConfigProperties = kafkaConfigProperties;

        Properties properties = new Properties();
        properties.put("bootstrap.servers", kafkaConfigProperties.getBootstrapServers());
        properties.put("key.deserializer", kafkaConfigProperties.getKeyDeserializer());
        properties.put("value.deserializer", kafkaConfigProperties.getValueDeserializer());

        properties.put("group.id", "person-consumer-group");
        properties.put("enable.auto.commit", "false");

        this.consumer = new KafkaConsumer<>(properties);
        this.objectMapper = new ObjectMapper();
    }

    //Possible Kafka Consumer Heartbeat/Session Management
    //Consume messages from the specified topic and offset, with the count limit
    public List<String> consumeFromTopic(String topicName, long offset, int count) {
        List<String> messages = new ArrayList<>();
        logger.info("Starting to consume messages from topic: {}, offset: {}, count: {}", topicName, offset, count);

        try {
            //Get partitions for the topic
            List<TopicPartition> partitions = new ArrayList<>();
            consumer.partitionsFor(topicName).forEach(partitionInfo ->
                    partitions.add(new TopicPartition(topicName, partitionInfo.partition()))
            );

            //Assign consumer to partitions
            consumer.assign(partitions);

            //Seek to the specified offset for each partition
            //Offset Management? scenarios where offsets could be invalid or out of range
            for (TopicPartition partition : partitions) {
                consumer.seek(partition, offset);
            }

            //Poll for messages
            int recordsConsumed = 0;
            while (recordsConsumed < count) {
                ConsumerRecords<String, String> records = consumer.poll(1000);

                for (ConsumerRecord<String, String> record : records) {
                    if (recordsConsumed >= count) {
                        break;
                    }

                    messages.add(record.value());
                    recordsConsumed++;
                }
            }
        } catch (Exception e) {
            logger.error("Error while consuming messages", e);
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            consumer.close();
        }
        logger.info("Returning {} messages", messages.size());
        return messages;
    }
}