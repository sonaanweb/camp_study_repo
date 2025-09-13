package com.sparta.myselectshop.entity;

import com.sparta.myselectshop.dto.ProductMypriceRequestDto;
import com.sparta.myselectshop.dto.ProductRequestDto;
import com.sparta.myselectshop.naver.dto.ItemDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity // JPA가 관리할 수 있는 Entity 클래스 지정
@Getter
@Setter
@Table(name = "product") // 매핑할 테이블의 이름을 지정
@NoArgsConstructor
public class Product extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private String link;

    @Column(nullable = false)
    private int lprice;

    @Column(nullable = false)
    private int myprice;

    /**
     * 상품 : 회원 = N : 1 = 한명의 회원은 다수의 상품을 가진다.
     * 연관관계 방향 => 회원 객체에서 상품 객체를 조회하는 경우는 없기 때문에 N:1 단방향
     */
    @ManyToOne(fetch = FetchType.LAZY) // 상품 조회 시 회원 정보가 항상 필요하지 않기 때문에 lazy
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * 상품이 1 : 폴더가 N
     * 주의 > mappedby(양방향) = FK를 가지고 있지 않은 쪽에서 사용
     * 다른 엔티티의 어떤 필드가 FK 주인인지 알려주는 것.
     * 타겟팅이 될 외래키의 주인(ProductFolder) 필드(주인의 필드 이름)이다 - join 필드명
     * => ProductFolder 안에서 FK를 가진 필드가 product 이므로
     * mappedBy에 적는 값은, FK를 실제로 가진 엔티티(ProductFolder)의 필드 이름을 가리킨다
     */
    @OneToMany(mappedBy = "product")
    private List<ProductFolder> productFolderList = new ArrayList<>();

    // 추후 builder 애너테이션 사용
    public Product(ProductRequestDto requestDto, User user) {
        this.title = requestDto.getTitle();
        this.image = requestDto.getImage();
        this.link = requestDto.getLink();
        this.lprice = requestDto.getLprice();
        this.user = user; // 받아올 유저
    }

    // update
    public void update(ProductMypriceRequestDto requestDto) {
        this.myprice = requestDto.getMyprice();
    }

    // 최저가 update
    public void updateByItemDto(ItemDto itemDto) {
        this.lprice = itemDto.getLprice();
    }
}
/**
 * Product를 조회할 때 마다 계속해서 무조건 productFolderList의 정보가 필요하다 싶으면 = 즉시 로딩
 * 때에 따라 필요할 수 있고 아닐 수 있으면 지연 로딩 선택
 */