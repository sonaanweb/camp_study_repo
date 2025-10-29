package com.market.order;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    // 큐 이름을 받아야 그 큐로 메세지가 가기 때문에 큐 이름이 필요하다
    @Value("${message.queue.product}")
    private String productQueue;

    @Value("${message.queue.payment}")
    private String paymentQueue;

    // RabbitMQ로 요청을 보낼 때 사용
    private final RabbitTemplate rabbitTemplate;

    public void createOrder(String orderId){
        rabbitTemplate.convertAndSend(productQueue, orderId);
        rabbitTemplate.convertAndSend(paymentQueue, orderId);
    }
}
