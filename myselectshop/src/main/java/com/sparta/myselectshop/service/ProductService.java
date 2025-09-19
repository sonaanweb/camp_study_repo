package com.sparta.myselectshop.service;

import com.sparta.myselectshop.dto.ProductMypriceRequestDto;
import com.sparta.myselectshop.dto.ProductRequestDto;
import com.sparta.myselectshop.dto.ProductResponseDto;
import com.sparta.myselectshop.entity.*;
import com.sparta.myselectshop.exception.ProductNotFoundException;
import com.sparta.myselectshop.naver.dto.ItemDto;
import com.sparta.myselectshop.repository.FolderRepository;
import com.sparta.myselectshop.repository.ProductFolderRepository;
import com.sparta.myselectshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final FolderRepository folderRepository;
    private final ProductFolderRepository productFolderRepository;

    private final MessageSource messageSource; // spring 구현체

    public static final int MIN_MY_PRICE = 100; // 최소 설정 가격

    // 관심 상품 등록 service
    public ProductResponseDto createProduct(ProductRequestDto requestDto, User user) {
        Product product = productRepository.save(new Product(requestDto, user));
        return new ProductResponseDto(product); // product 데이터를 dto로 반환
    }

    // 관심 상품 희망 최저가 등록하기 service
    @Transactional // 변경 감지
    public ProductResponseDto updateProduct(Long id, ProductMypriceRequestDto requestDto, User user){

        // 1. 요청 가격 가져옴
        int myprice = requestDto.getMyprice();

        // 2. 최소 입력 값 100원
        if(myprice < MIN_MY_PRICE) {
            throw new IllegalArgumentException(
                    messageSource.getMessage(
                            "below.min.my.price", // 설정한 code
                            new Integer[]{MIN_MY_PRICE}, // 배열로 0번째 값
                            "Wrong Price", // default message
                            Locale.getDefault() // 기본 언어설정
                    )
            );
        }

        // 3. 상품 유효성 확인
        Product product = productRepository.findById(id).orElseThrow(() ->
                new ProductNotFoundException(messageSource.getMessage( // custom exception
                        "not found product",
                        null,
                        "Not Found Product",
                        Locale.getDefault()
                ))
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

    // 상품 - 폴더에 추가
    public void addFolder(Long productId, Long folderId, User user) {

        // 상품 유효성 검사
        Product product = productRepository.findById(productId).orElseThrow(
                ()-> new NullPointerException("해당 상품이 존재하지 않습니다.")
        );

        // 폴더 유효성 검사
        Folder folder = folderRepository.findById(folderId).orElseThrow(
                ()-> new NullPointerException("해당 폴더가 존재하지 않습니다.")
        );

        // 유저 일치 확인
        if(!product.getUser().getId().equals(user.getId())
        || !folder.getUser().getId().equals(user.getId())) {

            throw new IllegalArgumentException("회원님의 관심상품이 아니거나, 회원님의 폴더가 아닙니다.");
        }

        // 폴더 내에 상품이 이미 등록되어 있는 지 중복확인을 해준다.
        Optional<ProductFolder> overlapFolder = productFolderRepository.findByProductAndFolder(product, folder);

        if (overlapFolder.isPresent()) {
            throw new IllegalArgumentException("중복된 폴더입니다.");
        }

        // 상품에 폴더 추가 완료
        /**
         * ProductFolder - Entity 클래스 객체 하나가 데이터베이스에서는 한줄이 된다.
         */
        productFolderRepository.save(new ProductFolder(product, folder));
    }

    // 폴더 내 상품 조회
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getProductsInFolder(Long folderId, int page, int size, String sortBy, boolean isAsc, User user) {

        // 페이징 처리
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        // 해당 폴더에 등록된 상품을 가져옵니다.
        Page<Product> products = productRepository.findAllByUserAndProductFolderList_FolderId(user, folderId, pageable);

        // 반환할 리스트
        Page<ProductResponseDto> responseDtoList = products.map(ProductResponseDto::new);

        return responseDtoList;
    }
}
