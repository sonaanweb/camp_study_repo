package com.sparta.myselectshop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 회원 정보 요청 API DTO
 */
@Getter
@AllArgsConstructor
public class UserInfoDto {
    String username; // username 확인
    boolean isAdmin; // role 확인 admin 유무
}