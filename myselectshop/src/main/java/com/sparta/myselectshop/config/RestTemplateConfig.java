package com.sparta.myselectshop.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig  {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        // Builder를 통해 직접 ClientHttpRequestFactory를 설정
        return builder
                .requestFactory(() -> new BufferingClientHttpRequestFactory(createClientHttpRequestFactory()))
                .build();
    }

    // ClientHttpRequestFactory 생성 및 타임아웃 설정 메서드
    private ClientHttpRequestFactory createClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();

        // RestTemplate 으로 외부 API 호출 시 일정 시간이 지나도 응답이 없을 때
        // 무한 대기 상태 방지를 위해 강제 종료 설정
        // 타임아웃 설정 (단위: milliseconds)
        factory.setConnectTimeout(5000); // 5초
        factory.setReadTimeout(5000);    // 5초

        return factory;
    }
}