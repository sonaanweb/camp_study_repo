package com.market.order;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.UUID;

/**
 * Order 객체
 * : 실제 프로젝트 진행시에는 Entity 명명하고 DB 관리
 * : status, type도 Enum으로 관리
 */
@Builder
@Data
@ToString
public class Order {

    private UUID orderId;
    private String userId;
    private String orderStatus;
    private String errorType;

    // 주문 취소
    public void cancelOrder(String receiveErrorType){
        orderStatus = "CANCELLED";
        errorType = receiveErrorType;
    }
}
