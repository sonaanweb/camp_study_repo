package com.example.redis.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializer;
import static org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;

import java.time.Duration;

@Configuration
@EnableCaching // 어노테이션 바탕으로 캐싱을 조절할 수 있게 해줌
public class CacheConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory, LettuceConnectionFactory redisConnectionFactory) {

        // 1. 설정 구성 진행
        // Redis를 이용해 Spring cache를 사용할 때, Redis 관련 설정을 모아두는 클래스
        RedisCacheConfiguration configuration = RedisCacheConfiguration
                // 기본 설정
                .defaultCacheConfig()
                // null은 캐싱 안 하겠다.
                .disableCachingNullValues()
                // 기본 캐시 유지 시간 (Time To Live) : 데이터가 들어갔을 때 특별한 추가 시간이 없으면 60초간만 유지
                .entryTtl(Duration.ofSeconds(60))
                // 캐시를 구분하는 접두사 설정
                .computePrefixWith(CacheKeyPrefix.simple())
                // 캐시에 저장할 값을 어떻게 직렬화 / 역직렬화 할 것인지 설정
                .serializeValuesWith(
                        SerializationPair.fromSerializer(RedisSerializer.java()));

        return RedisCacheManager
                .builder(redisConnectionFactory)
                .cacheDefaults(configuration) // 위에 값을 `캐싱 기본`으로 설정
                // .withInitialCacheConfigurations() -> map을 인자로 받는다(cacheNames)
                .build();
    }
}
