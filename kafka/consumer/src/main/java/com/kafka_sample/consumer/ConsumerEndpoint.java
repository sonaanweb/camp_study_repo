package com.kafka_sample.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConsumerEndpoint {

    // @KafkaListener 어노테이션은 이 메서드를 Kafka 리스너로 설정합니다.
    // Kafka 토픽 "test-topic"에서 메시지를 수신하면 이 메서드가 호출됩니다.
    // groupId는 컨슈머 그룹을 지정하여 동일한 그룹에 속한 다른 컨슈머와 메시지를 분배받습니다.

    /**
     * 토픽이 동일하고 그룹이 다른 메서드
     */
    // 이 메서드는 Kafka에서 메시지를 소비하는 리스너 메서드입니다.
    @KafkaListener(groupId = "group_a", topics = "topic1")
    public void consumeFromGroupA(String message) {
        log.info("Group A consumed message from topic1: " + message);
    }

    // `동일한 토픽`을 `다른 그룹 ID로 소비`하는 또 다른 리스너 메서드입니다.
    @KafkaListener(groupId = "group_b", topics = "topic1")
    public void consumeFromGroupB(String message) {
        log.info("Group B consumed message from topic1: " + message);
    }


    /**
     * 그룹은 같은데 토픽이 다른 메서드
     */
    // `다른 토픽을 다른 그룹 ID로 소비`하는 리스너 메서드입니다.
    @KafkaListener(groupId = "group_c", topics = "topic2")
    public void consumeFromTopicC(String message) {
        log.info("Group C consumed message from topic2: " + message);
    }

    @KafkaListener(groupId = "group_c", topics = "topic3")
    public void consumeFromTopicD(String message) {
        log.info("Group C consumed message from topic3: " + message);
    }


    /**
     * 그룹과 토픽이 전부 다른 메서드
     */
    @KafkaListener(groupId = "group_d", topics = "topic4")
    public void consumeFromPartition0(String message) {
        log.info("Group D consumed message from topic4: " + message);
    }
}
