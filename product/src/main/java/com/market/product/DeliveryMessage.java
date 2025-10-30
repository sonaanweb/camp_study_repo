package com.market.product;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryMessage {

    // 프로세스 상에서 입력
    private UUID orderId;
    private UUID paymentId;

    // order 주문할 때 받는 것
    private String userId;
    private Integer productId;
    private Integer productQuantity;
    private Integer payAmount;

    // 프로세스 상에서 입력
    private String errorType;
}

/**
 * 이 객체를 큐로 보내야 한다.
 */