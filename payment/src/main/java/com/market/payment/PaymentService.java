package com.market.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final RabbitTemplate rabbitTemplate;

    @Value("${message.queue.err.product}")
    private String productErrorQueue;

    // 결제 생성 - (모든 로직은 테스트를 위한 테스트 코드)
    public void createPayment(DeliveryMessage deliveryMessage) {
        Payment payment = Payment.builder()
                .paymentId(UUID.randomUUID())
                .userId(deliveryMessage.getUserId())
                .payAmount(deliveryMessage.getPayAmount())
                .payStatus("SUCCESS").build(); // 임의

        if (payment.getPayAmount() >= 10000) { // 10000원 이상일 시 에러
            log.error("Payment amount exceeds limit: {}", payment.getPayAmount());
            payment.setPayStatus("CANCEL");
            deliveryMessage.setErrorType("PAYMENT_LIMIT_EXCEEDED"); // 에러 타입 기록
            this.rollbackPayment(deliveryMessage); // 롤백 호출
        }
    }


    // 에러 큐로 보내는 함수
    // 에러가 나면 에러큐에 데이터가 쌓이는 걸 확인할 수 있다.
    public void rollbackPayment(DeliveryMessage deliveryMessage) {
        log.info("PAYMENT ROLLBACK !!!");
        rabbitTemplate.convertAndSend(productErrorQueue, deliveryMessage);
    }
}
