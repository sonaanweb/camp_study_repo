package com.sparta.myselectshop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "api_use_time")
public class ApiUseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne // 1:1 유저 하나가 하나의 누적시간을 가짐
    @JoinColumn(name = "user_id", nullable = false) // 단방향
    private User user;

    // 전체 시간 누적
    @Column(nullable = false)
    private Long totalTime;

    public ApiUseTime(User user, Long totalTime) {
        this.user = user;
        this.totalTime = totalTime;
    }

    // add += 누적 계산 위함
    public void addUseTime(long useTime) {
        this.totalTime += useTime;
    }
}