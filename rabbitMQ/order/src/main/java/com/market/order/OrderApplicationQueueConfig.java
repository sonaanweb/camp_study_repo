package com.market.order;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderApplicationQueueConfig {

    @Value(("${message.exchange}"))
    private String exchange;

    @Value("${message.queue.product}")
    private String queueProduct;

    @Value("${message.queue.payment}")
    private String queuePayment;


    // 익스체인지(exchange) 생성
    @Bean public TopicExchange exchange() {return new TopicExchange(exchange);}

    // 큐(queue) 생성
    @Bean public Queue queueProduct() {return new Queue(queueProduct);}
    @Bean public Queue queuePayment() {return new Queue(queuePayment);}


    // 바인딩 생성 - 바인딩 이름은 큐의 이름과 일치시킨다.
    /**
     * bind.(어느 큐로 이동할 건지)
     * to(exchange) 큐에는 익스체인지 넣어줬어야 함 <market>
     * with(바인딩 이름) = 큐 이름과 일치시켜줌
     */
    @Bean public Binding bindingProduct(){return BindingBuilder.bind(queueProduct()).to(exchange()).with(queueProduct);}
    @Bean public Binding bindingPayment() { return BindingBuilder.bind(queuePayment()).to(exchange()).with(queuePayment); }
}
