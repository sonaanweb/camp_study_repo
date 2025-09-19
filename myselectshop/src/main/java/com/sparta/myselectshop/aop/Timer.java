package com.sparta.myselectshop.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * RUNTIME : 검색 API 실행할 때 aop 사용
 */
@Target({ElementType.TYPE, ElementType.METHOD}) // Timer - 클래스 , 메서드에 붙이겠다
@Retention(RetentionPolicy.RUNTIME) // 애너테이션의 생명주기- RUNTIME에도 유지
public @interface Timer {
}
