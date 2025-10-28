package com.sparta.springresttemplateclient.naver.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.springresttemplateclient.naver.dto.ItemDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j(topic = "NAVER API")
@Service
@RequiredArgsConstructor
public class NaverApiService {

    private final RestTemplateBuilder restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper(); // jackson

    public List<ItemDto> searchItems(String query) {
        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
                .fromUriString("https://openapi.naver.com")
                .path("/v1/search/shop.json") // 요청 주소
                .queryParam("display", 15) // 갯수
                .queryParam("query", query) // 검색어
                .encode()
                .build()
                .toUri();

        log.info("uri = " + uri);

        // get - body(x) VOID
        RequestEntity<Void> requestEntity = RequestEntity
                .get(uri)
                .header("X-Naver-Client-Id", "3S4MZ24INFdOwTNwArpq")
                .header("X-Naver-Client-Secret", "RoXLcweEIT")
                .build();

        ResponseEntity<String> responseEntity = restTemplate.build().exchange(requestEntity, String.class);

        log.info("NAVER API Status Code : " + responseEntity.getStatusCode());

        return fromJSONtoItems(responseEntity.getBody());
    }

    // Naver API 응답(JSON 문자열) -> 자바 객체 List<ItemDto> 로 변환파싱
    private List<ItemDto> fromJSONtoItems(String response) {
        try {
            // json 문자열을 jackson 트리 구조로 변환
            JsonNode root = objectMapper.readTree(response);
            // items 배열 추출
            JsonNode items = root.get("items");
            // jsonNode -> List<ItemDto>로 한 번에 매핑
            return objectMapper.convertValue(
                    items,
                    new TypeReference<List<ItemDto>>() {}
            );
        } catch (Exception e) {
            throw new RuntimeException("NAVER API 응답 파싱 실패", e);
        }
    }
}