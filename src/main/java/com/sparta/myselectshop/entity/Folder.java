package com.sparta.myselectshop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "folder")
public class Folder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    /**
     * 폴더와 회원 = N:1 관계 -> 회원은 폴더를 여러개 등록 가능
     * 회원 객체에서 폴더 객체를 조회하는 경우는 없기 때문에 N:1 단방향 설정
     */
    @ManyToOne(fetch = FetchType.LAZY) // 폴더 조회할 때 회원을 매번 가져오지 않으므로 lazy
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Folder(String name, User user) {
        this.name = name;
        this.user = user;
    }
}