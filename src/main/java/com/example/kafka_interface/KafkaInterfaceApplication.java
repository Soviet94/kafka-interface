package com.example.kafka_interface;

import com.example.kafka_interface.configuration.KafkaConfigProperties;
import com.example.kafka_interface.producer.KafkaProducerService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class KafkaInterfaceApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(KafkaInterfaceApplication.class, args);

		//load the records
		// Get the KafkaProducerService from Spring context
		KafkaProducerService kafkaProducerService = context.getBean(KafkaProducerService.class);

		try {
			// Call loadJsonToKafka to load the records from the JSON file and send them to Kafka
			kafkaProducerService.loadJsonToKafka();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// Close the producer after the task is done
			kafkaProducerService.close();
		}
	}

}
