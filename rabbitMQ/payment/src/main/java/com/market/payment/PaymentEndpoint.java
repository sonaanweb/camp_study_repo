package com.market.payment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PaymentEndpoint {

    @Value("${spring.application.name}")
    private String appName;

    // 큐를 바라보는 리스너
    // 동일한 문법을 작성해주면 value를 선언하지 않아도 가져올 수 있음
    @RabbitListener(queues = "${message.queue.payment}")
    public void receiveMessage(String orderId) {
        log.info("receive orderId:{}, appName : {}", orderId, appName);
    }
}
