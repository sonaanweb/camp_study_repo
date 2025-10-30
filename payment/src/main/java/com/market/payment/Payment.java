package com.market.payment;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

/**
 * Payment
 * : 마찬가지로 실제 프로젝트 진행시에는 Entity 명명하고 DB 관리
 * : status Enum으로 관리
 */
@Data
@Builder
public class Payment {
    private UUID paymentId;
    private String userId;

    private Integer payAmount;

    private String payStatus;
}
