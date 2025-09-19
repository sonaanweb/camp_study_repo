package com.sparta.myselectshop.exception;

// exception : 상속 받으면 반환할 수 있다.
// custom exception
public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String message) { // 생성자 메세지 받아옴
        super(message); // 런타임 exception 쪽으로 메세지 전달 (부모)
    }
}
