package com.kafka_sample.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String topic , String key, String message) {
        // 메세지 10개 발행 로직
        for (int i = 0; i < 10; i++) {

            kafkaTemplate.send(topic, key, message + " " + i);
        }

    }
}
