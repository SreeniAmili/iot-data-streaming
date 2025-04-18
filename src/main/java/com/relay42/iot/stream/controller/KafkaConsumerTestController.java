package com.relay42.iot.stream.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/kafka")
public class KafkaConsumerTestController {

    private volatile String lastMessage = "No message consumed yet.";

    @KafkaListener(topics = "demo-topic", groupId = "rest-consumer")
    public void listen(String message) {
        System.out.println("Consumed from Kafka: " + message);
        lastMessage = message;
    }

    @GetMapping("/consume")
    public ResponseEntity<String> getLastMessage() {
        return ResponseEntity.ok("Last consumed message: " + lastMessage);
    }
}
