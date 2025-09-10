package com.sparta.springresttemplateserver.controller;

import com.sparta.springresttemplateserver.dto.ItemResponseDto;
import com.sparta.springresttemplateserver.dto.UserRequestDto;
import com.sparta.springresttemplateserver.entity.Item;
import com.sparta.springresttemplateserver.service.ItemService;
import org.springframework.web.bind.annotation.*;

/**
 * server
 * 포스트맨 요청이 아닌 클라이언트 서버 입장(resttemplate)에서 보내온 요청을 받는 컨트롤러
 */
@RestController
@RequestMapping("/api/server")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/get-call-obj")
    public Item getCallObject(@RequestParam String query) {
        return itemService.getCallObject(query);
    }

    @GetMapping("/get-call-list")
    public ItemResponseDto getCallList() {
        return itemService.getCallList();
    }

    @PostMapping("/post-call/{query}")
    public Item postCall(@PathVariable String query, @RequestBody UserRequestDto requestDto) {
        return itemService.postCall(query, requestDto);
    }

    @PostMapping("/exchange-call")
    public ItemResponseDto exchangeCall(@RequestHeader("X-Authorization") String token, @RequestBody UserRequestDto requestDto) {
        return itemService.exchangeCall(token, requestDto);
    }
}