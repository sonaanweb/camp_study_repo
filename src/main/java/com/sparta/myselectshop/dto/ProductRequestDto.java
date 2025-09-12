package com.sparta.myselectshop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * naver api로 보내지는 요청
 * 사용자가 웹/앱에서 검색어 입력 -> 서버로 요청 -> [서버] -> Open API 요청
 * -> JSON 응답 반환 -> 서버가 클라이언트에게 응답 반환
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {
    // 관심상품명
    private String title;
    // 관심상품 썸네일 image URL
    private String image;
    // 관심상품 구매링크 URL
    private String link;
    // 관심상품의 최저가
    private int lprice;
}

/**
 * 핵심 정리
 * ItemDto -> 외부 Open API에서 가져온 데이터 구조 그대로 사용
 * ProductRequestDto -> 내부 로직/ DB와 통신할 때 사용
 */