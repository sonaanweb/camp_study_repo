package com.market.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * 외부에서 요청을 받고
 * product 큐에 메세지를 전달하는 역할을 하는 OrderEndpoint
 *
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class OrderEndpoint {

    private final OrderService orderService;

    // 조회
    @GetMapping("/order/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable("orderId") UUID orderId){
        Order order = orderService.getOrder(orderId);
        return ResponseEntity.ok(order);
    }

    // 생성
    @PostMapping("/order")
    public ResponseEntity<Order> order(@RequestBody OrderReqDto orderReqDto){
        Order order = orderService.createOrder(orderReqDto);
        return ResponseEntity.ok(order);
    }




    /**
     * order 요청 DTO - 실제 프로젝트에선 분리
     */
    public static class OrderReqDto {
        private String userId;
        private Integer productId;
        private Integer productQuantity;
        private Integer payAmount;

        public Order toOrder() {
            return Order.builder()
                    .orderId(UUID.randomUUID())
                    .userId(userId)
                    .orderStatus("RECEIPT")
                    .build();
        }

        /**
         * DeliveryMessage
         */
        public DeliveryMessage toDeliveryMessage(UUID orderId){
            return DeliveryMessage.builder()
                    .orderId(orderId) // 전달 받은 orderId
                    .productId(productId) // 구매 상품 Id
                    .productQuantity(productQuantity)
                    .payAmount(payAmount)
                    .build();
        }

    }

}
