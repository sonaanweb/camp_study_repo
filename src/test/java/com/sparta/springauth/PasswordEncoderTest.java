package com.sparta.springauth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class PasswordEncoderTest {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("수동 등록한 passwordEncoder를 주입 받아옴")
    void test1(){

        String password = "Robbie's password";

        // 암호화
        String encodedPassword = passwordEncoder.encode(password);
        System.out.println("encodedPassword = " + encodedPassword);

        String inputPassword = "Robbie"; // 평문

        // 해시된 비밀번호와 사용자가 입력한 비밀번호를 해싱한 값 비교
        boolean matches = passwordEncoder.matches(inputPassword, encodedPassword);
        System.out.println("matches = " + matches); // 암호화할 때 사용된 값과 다른 문자열과 비교했기 때문에 false
    }
}

/**
 * matches(입력 문자열, 비교할 문자열) - encoder에서 메서드 제공: 내부에서 비교해줌
 */