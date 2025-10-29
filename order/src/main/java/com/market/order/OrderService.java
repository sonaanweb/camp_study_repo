package com.market.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    @Value("${message.queue.product}")
    private String productQueue;

    private final RabbitTemplate rabbitTemplate;

    // 메모리 상에 저장하는 거로 진행!
    private Map<UUID, Order> orderStore = new HashMap<>();

    public Order createOrder(OrderEndpoint.OrderReqDto orderReqDto) {

        // order 객체 생성
        Order order = orderReqDto.toOrder();

        // deliveryMessage
        DeliveryMessage deliveryMessage = orderReqDto.toDeliveryMessage(order.getOrderId());

        orderStore.put(order.getOrderId(), order);

        log.info("send Message : {}",deliveryMessage.toString());

        // 이 메세지를 만든 DeliveryMessage를 product Queue에 보내줌
        rabbitTemplate.convertAndSend(productQueue, deliveryMessage);

        return order;
    }

    // order 객체 반환
    public Order getOrder(UUID orderId){
        return orderStore.get(orderId);
    }
}
