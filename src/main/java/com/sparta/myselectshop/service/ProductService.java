package com.sparta.myselectshop.service;

import com.sparta.myselectshop.dto.ProductMypriceRequestDto;
import com.sparta.myselectshop.dto.ProductRequestDto;
import com.sparta.myselectshop.dto.ProductResponseDto;
import com.sparta.myselectshop.entity.Product;
import com.sparta.myselectshop.entity.User;
import com.sparta.myselectshop.entity.UserRoleEnum;
import com.sparta.myselectshop.naver.dto.ItemDto;
import com.sparta.myselectshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public static final int MIN_MY_PRICE = 100; // 최소 설정 가격

    // 관심 상품 등록 service
    public ProductResponseDto createProduct(ProductRequestDto requestDto, User user) {
        Product product = productRepository.save(new Product(requestDto, user));
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

    // 관심상품 리스트 조회 - 반환 타입 PAGE
    @Transactional(readOnly = true) // 지연로딩 기능 문제 없이 사용하기 위함
    public Page<ProductResponseDto> getProducts(User user, int page, int size, String sortBy, boolean isAsc) {

        // true = asc / false = desc
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        // 유저 - 관리자 확인
        UserRoleEnum userRoleEnum = user.getRole();

        Page<Product> productList;

        if(userRoleEnum.equals(UserRoleEnum.USER)){ // 일반 계정
            productList = productRepository.findAllByUser(user, pageable);
        } else { // 어드민
            productList = productRepository.findAll(pageable);
        }

        return productList.map(ProductResponseDto::new);

        /** stream 사용 코드 -> 반복문 없이 데이터 변환, 필터링, 집계 가능
         * public List<ProductResponseDto> getProducts() {
         *     return productRepository.findAll().stream()
         *             .map(ProductResponseDto::new) // Product -> ProductResponseDto 변환
         *             .collect(Collectors.toList());
         * }
         */
    }

    // 최저가 업데이트
    @Transactional
    public void updateBySearch(Long id, ItemDto itemDto) {
        Product product = productRepository.findById(id).orElseThrow(()->
                new NullPointerException("해당 상품은 존재하지 않습니다")
        );
        product.updateByItemDto(itemDto);
    }
}
