package com.sparta.springauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordConfig {

    @Bean
    public PasswordEncoder passwordEncoder() { // 빈 등록
        return new BCryptPasswordEncoder(); // 구현체 인터페이스 -해시함수
    }
}

/**
 * 스프링 서버가 뜰 때 spring ioc 컨테이너에서 bean으로 저장됨
 * 가져다 사용하면 (di 주입 받으면) 구현체 등록/주입
 */