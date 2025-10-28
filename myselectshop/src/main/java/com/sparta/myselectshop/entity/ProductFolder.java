package com.sparta.myselectshop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 외래키의 주인 - FK를 실제로 가지고 있는 엔티티
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "product_folder")
public class ProductFolder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id", nullable = false)
    private Folder folder;

    public ProductFolder(Product product, Folder folder) {
        this.product = product;
        this.folder = folder;
    }
}

/**
 * 1. 상품은 추가될 폴더의 정보가 필요함
 * 폴더 : 상품 = N : 1 -> 상품은 여러 개의 폴더를 등록할 수 있다.
 *
 * 2. 폴더에 관심 상품을 등록
 * 폴더 : 상품 = 1 : N -> 하나의 폴더는 여러 개의 상품을 포함한다
 *
 * 결과적으로 상품과 폴더는 N:M 다대다 관계.
 * 상품_폴더 entity 중간 테이블을 만들어 관리
 *
 * 연관 관계 방향
 * - 관심 상품 조회 시 => 포함된 폴더들의 정보가 필요함 (상품 객체가 폴더 객체 조회 필요)
 * - 상품 객체를 기준으로 해당 폴더에 포함된 상품들을 조회해야함 (상품 객체가 폴더 객체 조회 필요)
 * - 따라서 상품과 상품_폴더는 양방향 연관 관계로 설정
 * - 폴더 객체에서 상품 객체를 조회하지 않을 프로젝트이기 때문에 폴더는 상품_폴더와 관계를 맺지 않는다.
 */