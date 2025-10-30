package com.market.order;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderApplicationQueueConfig {

    // jackson
    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    // properties value
    @Value("${message.exchange}")
    private String exchange;

    @Value("${message.queue.product}")
    private String queueProduct;

    @Value("${message.queue.payment}")
    private String queuePayment;

    @Value("${message.err.exchange}")
    private String exchangeErr;

    @Value("${message.queue.err.order}")
    private String queueErrOrder;

    @Value("${message.queue.err.product}")
    private String queueErrProduct;


    /**
     * 정상 프로세스
     */
    // 1. exchange 생성 - 공통 (market exchange)
    @Bean public TopicExchange exchange(){return new TopicExchange(exchange);}

    // 2. Queue 생성 (market.product, market.payment)
    @Bean public Queue queueProduct() { return new Queue(queueProduct); }
    @Bean public Queue queuePayment() { return new Queue(queuePayment); }

    // 3. Binding 설정 (exchange -> queue로 라우팅할 때 실제로 exchange는 바인딩을 통해 보내주므로)
    // 바인딩의 이름은 큐의 이름과 동일하게 만든다. (라우팅 키 = market.product)
    // 3-2. order -- <binding> --> market.product 큐로
    @Bean public Binding bindingProduct() { return BindingBuilder.bind(queueProduct()).to(exchange()).with(queueProduct); }
    // 3-3. product -- <binding> --> market.payment 큐로
    @Bean public Binding bindingPayment() { return BindingBuilder.bind(queuePayment()).to(exchange()).with(queuePayment); }


    /**
     * Error 프로세스
     */
    // 1. Err_exchange 생성 - 공통 (market err.exchange)
    @Bean public TopicExchange exchangeErr(){return new TopicExchange(exchangeErr);}

    // 2. Err_Queue 생성 (market.err.order, market.err.product)
    @Bean public Queue queueErrOrder(){return new Queue(queueErrOrder);}
    @Bean public Queue queueErrProduct(){return new Queue(queueErrProduct);}

    // 3. Binding 설정
    // 3-2. payment -- <binding> --> market.err.product 큐로
    @Bean public Binding bindingErrOrder(){ return BindingBuilder.bind(queueErrOrder()).to(exchangeErr()).with(queueErrOrder); }
    // 3-3. product -- <binding> --> market.err.order 큐로
    @Bean public Binding bindingErrProduct(){ return BindingBuilder.bind(queueErrProduct()).to(exchangeErr()).with(queueErrProduct); }

}
