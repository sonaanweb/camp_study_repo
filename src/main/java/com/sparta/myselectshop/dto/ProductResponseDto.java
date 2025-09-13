package com.sparta.myselectshop.dto;

import com.sparta.myselectshop.entity.Product;
import com.sparta.myselectshop.entity.ProductFolder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ProductResponseDto {
    private Long id;
    private String title;
    private String link;
    private String image;
    private int lprice;
    private int myprice;

    private List<FolderResponseDto> productFolderList = new ArrayList<>();

    public ProductResponseDto(Product product) {
        this.id = product.getId();
        this.title = product.getTitle();
        this.link = product.getLink();
        this.image = product.getImage();
        this.lprice = product.getLprice();
        this.myprice = product.getMyprice();

        for (ProductFolder productFolder : product.getProductFolderList()) {
            productFolderList.add(new FolderResponseDto(productFolder.getFolder()));
        }
    }
}

/**
 * Product에 우리가 연결한 중간 테이블 - ProductFolderList를 가지고 옵니다.
 * 그곳에 있는 폴더 정보를 FolderResponseDto로 변환하면서 집어넣는다.
 * 이 리스트에는 결국 폴더의 정보가 담기게 된다.
 */