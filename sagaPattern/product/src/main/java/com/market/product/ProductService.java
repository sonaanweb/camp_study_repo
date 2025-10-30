package com.market.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final RabbitTemplate rabbitTemplate;

    @Value("${message.queue.payment}")
    private String paymentQueue;

    @Value("${message.queue.err.order}")
    private String orderErrorQueue;


    // (-) 수량
    public void reduceProductAmount(DeliveryMessage deliveryMessage) {

        Integer productId = deliveryMessage.getProductId();
        Integer productQuantity = deliveryMessage.getProductQuantity();

        /**
         * 데이터 임시 설정
         * productId = 1이 아니거나 수량이 1이상이면 err
         */
        if (productId != 1 || productQuantity > 1) {
            this.rollbackProduct(deliveryMessage);
            return;
        }

        rabbitTemplate.convertAndSend(paymentQueue,deliveryMessage);
    }


    // 롤백 함수
    public void rollbackProduct(DeliveryMessage deliveryMessage){
        log.info("PRODUCT ROLLBACK!!!");
        if(!StringUtils.hasText(deliveryMessage.getErrorType())){
            // 에러가 기록되지 않은 order로부터 전달받은 deliveryMessage가 있는 상태
            deliveryMessage.setErrorType("PRODUCT ERROR");
        }
        rabbitTemplate.convertAndSend(orderErrorQueue, deliveryMessage);
    }

}

/**
 * productId나 수량을 잘못 입력해 에러가 난 경우에는
 * 바로 에러를 내고 order error 큐로 전달하는 동작
 * -> product에서 에러가 났는데 payment로 갈 이유가 없으므로
 */