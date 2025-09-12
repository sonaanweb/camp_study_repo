package com.sparta.myselectshop.service;

import com.sparta.myselectshop.dto.ProductMypriceRequestDto;
import com.sparta.myselectshop.dto.ProductRequestDto;
import com.sparta.myselectshop.dto.ProductResponseDto;
import com.sparta.myselectshop.entity.Product;
import com.sparta.myselectshop.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public static final int MIN_MY_PRICE = 100; // 최소 설정 가격

    // 관심 상품 등록 service
    public ProductResponseDto createProduct(ProductRequestDto requestDto) {
        Product product = productRepository.save(new Product(requestDto));
        return new ProductResponseDto(product); // product 데이터를 dto로 반환
    }

    // 관심 상품 희망 최저가 등록하기 service
    @Transactional // 변경 감지
    public ProductResponseDto updateProduct(Long id, ProductMypriceRequestDto requestDto){

        // 1. 요청 가격 가져옴
        int myprice = requestDto.getMyprice();

        // 2. 최소 입력 값 100원
        if(myprice < MIN_MY_PRICE) {
            throw new IllegalArgumentException("유효하지 않은 가격입니다. 최소" + MIN_MY_PRICE + "원 이상으로 설정해주세요");
        }

        // 3. 상품 유효성 확인
        Product product = productRepository.findById(id).orElseThrow(() ->
                new NullPointerException("해당 상품을 찾을 수 없습니다.")
        );

        // 4. update 완료
        product.update(requestDto);

        return new ProductResponseDto(product);
    }
}
