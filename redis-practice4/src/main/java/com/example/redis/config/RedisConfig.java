package com.example.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
@EnableRedisHttpSession
public class RedisConfig {
    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        return RedisSerializer.json();
    }
}

/**
 * JSON Serializer가 SecurityContext 내부 클래스를 정상적으로 생성자 없이 역직렬화하지 못해 "Cannot construct instance" 에러가 발생합니다.
 * SecurityContext를 직접 직렬화·역직렬화하거나, 적절한 생성자 및 filter 구현이 없다면, 현 상태에서는 세션 클래스터링에서 SecurityContext를 JSON 형태로 쓰기 어렵다는 점을 주의해야 합니다.
 */