# kafka-interface

## Description
Basic Kafka Producer and Consumer examples

## Prerequisites

Before running the program, you need to have **Apache Kafka** set up and running. The easiest way to get Kafka running is by using Docker.

### Step 1: Install Docker

Ensure that you have Docker installed on your machine. You can download and install Docker from [here](https://www.docker.com/products/docker-desktop).

### Step 2: Run Kafka using Docker

To start Kafka using Docker, run the following command in your terminal:

```bash
docker run -p 9092:9092 apache/kafka:3.7.0
```
Or run it from Docker Desktop

### Step 3: Configure Kafka

The program was created with the assumption of a 3 partitioned topic called "people". It was done with the following command

```bash
docker exec -it <CONTAINER_NAME> /opt/kafka/bin/kafka-topics.sh --create --topic people --partitions 3 --replication-factor 1 --bootstrap-server localhost:9092
```

You can configure your own but be sure to update the application.properties

### Step 4: Input Data

Default Input data is available in the resources as [random-people-data.json](src/main/resources/random-people-data.json). If the setup is correct it will be loaded in and made available for consuming on project start.

### TODO

Security

Concurrency

Filtering

Unit Testing
