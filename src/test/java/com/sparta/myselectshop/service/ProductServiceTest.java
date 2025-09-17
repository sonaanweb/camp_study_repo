package com.sparta.myselectshop.service;

import com.sparta.myselectshop.dto.ProductMypriceRequestDto;
import com.sparta.myselectshop.dto.ProductRequestDto;
import com.sparta.myselectshop.dto.ProductResponseDto;
import com.sparta.myselectshop.entity.Product;
import com.sparta.myselectshop.entity.User;
import com.sparta.myselectshop.repository.FolderRepository;
import com.sparta.myselectshop.repository.ProductFolderRepository;
import com.sparta.myselectshop.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class) // @Mock 사용을 위해 설정합니다.
class ProductServiceTest {

    /**
     * service-repo 분리를 위한 테스트 방법 - mock
     * 외부 의존성(repo, service, 외부 api) 같은 것에 쓰임
     * 가짜 객체로 만들고 싶은 클래스 / 애너테이션 설정 - 이후 생성자에 주입
     */

    @Mock
    ProductRepository productRepository;

    @Mock
    FolderRepository folderRepository;

    @Mock
    ProductFolderRepository productFolderRepository;

    MessageSource messageSource = mock(MessageSource.class);

    // SERVICE - updateProduct 메서드를 확인할 것이다.
    @Test
    @DisplayName("관심 상품 희망가 - 최저가 이상으로 변경")
    void test1() {
        // given
        Long productId = 100L;
        int myprice = ProductService.MIN_MY_PRICE + 3_000_000;

        ProductMypriceRequestDto requestMyPriceDto = new ProductMypriceRequestDto();
        requestMyPriceDto.setMyprice(myprice);

        /**
         * update mock 테스트를 위한 객체 생성이 필요함 user + dto - - product
         */
        // USER + DTO
        User user = new User();
        ProductRequestDto requestProductDto = new ProductRequestDto(
                "Apple <b>맥북</b> <b>프로</b> 16형 2021년 <b>M1</b> Max 10코어 실버 (MK1H3KH/A) ",
                "https://shopping-phinf.pstatic.net/main_2941337/29413376619.20220705152340.jpg",
                "https://search.shopping.naver.com/gate.nhn?id=29413376619",
                3515000
        );

        // product 객체 생성
        Product product = new Product(requestProductDto, user);

        ProductService productService = new ProductService(productRepository, folderRepository, productFolderRepository,messageSource);

        given(productRepository.findById(productId)).willReturn(Optional.of(product)); // willReturn - product 반환

        // when
        ProductResponseDto result = productService.updateProduct(productId, requestMyPriceDto, user);

        // then
        assertEquals(myprice, result.getMyprice());
    }

    @Test
    @DisplayName("관심 상품 희망가 - 최저가 미만으로 변경")
    void test2() {

        /* 확인하고 싶은 부분 -> GIVEN이 필요없다!(repo [객체 탐색] 접근 전 예외 발생)
        if(myprice < MIN_MY_PRICE) { 최저가 미만
            throw new IllegalArgumentException("유효하지 않은 가격입니다. 최소" + MIN_MY_PRICE + "원 이상으로 설정해주세요");
        }
        */

        User user = new User();

        // given
        Long productId = 200L;
        int myprice = ProductService.MIN_MY_PRICE - 50; // 최저가 미만

        ProductMypriceRequestDto requestMyPriceDto = new ProductMypriceRequestDto();
        requestMyPriceDto.setMyprice(myprice);

        ProductService productService = new ProductService(productRepository, folderRepository, productFolderRepository,messageSource);

        // when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.updateProduct(productId, requestMyPriceDto, user); // 실제 실행 코드
        });

        // then - 메세지 체크까지
        assertEquals(
                "유효하지 않은 가격입니다. 최소" +ProductService.MIN_MY_PRICE + "원 이상으로 설정해주세요",
                exception.getMessage()
        );
    }
}