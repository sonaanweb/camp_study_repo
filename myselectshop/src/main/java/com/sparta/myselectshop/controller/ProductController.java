package com.sparta.myselectshop.controller;

import com.sparta.myselectshop.dto.ProductMypriceRequestDto;
import com.sparta.myselectshop.dto.ProductRequestDto;
import com.sparta.myselectshop.dto.ProductResponseDto;
import com.sparta.myselectshop.security.UserDetailsImpl;
import com.sparta.myselectshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    // 관심 상품 등록하기
    @PostMapping("/products")
    public ProductResponseDto createProduct(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @RequestBody ProductRequestDto requestDto) {
        return productService.createProduct(requestDto, userDetails.getUser());
    }

    // 관심 상품 희망 최저가 등록하기
    @PutMapping("/products/{id}")
    public ProductResponseDto updateProduct(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @PathVariable Long id, @RequestBody ProductMypriceRequestDto requestDto) {

        // 응답 보내기
        return productService.updateProduct(id, requestDto, userDetails.getUser());
    }

    // 관심 상품 리스트 조회
    @GetMapping("/products")
    public Page<ProductResponseDto> getProducts(
            @RequestParam("page") int page, // 페이지 번호 - server = 0 부터 시작
            @RequestParam("size") int size, // 한 페이지 내 개수
            @RequestParam("sortBy") String sortBy, // 정렬 항목
            @RequestParam("isAsc") boolean isAsc, // 오름차순(true) / 내림차순(false)
            @AuthenticationPrincipal UserDetailsImpl userDetails){

        return productService.getProducts(userDetails.getUser(),
                page-1, size, sortBy, isAsc); // 페이지 번호 - client = 1 부터 이므로 -1
    }

    // 상품에 폴더 추가
    @PostMapping("/products/{productId}/folder")
    public void addFolder(@PathVariable Long productId,
                          @RequestParam Long folderId,
                          @AuthenticationPrincipal UserDetailsImpl userDetails){

        productService.addFolder(productId, folderId, userDetails.getUser());
        log.info("add folder controller productId={},folderId={},userId={} ", productId, folderId, userDetails.getUser().getId());
    }

    // 회원이 등록한 폴더 내 모든 상품 조회
    @GetMapping("/folders/{folderId}/products")
    public Page<ProductResponseDto> getProductsInFolder(
            @PathVariable Long folderId,
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String sortBy,
            @RequestParam boolean isAsc,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return productService.getProductsInFolder(
                folderId, page-1, size, sortBy, isAsc, userDetails.getUser()
        );
    }
}
