package com.sparta.myselectshop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass // JPA 공통 매핑 정보를 다른 엔티티 클래스에 상속받아 사용할 수 있도록
@EntityListeners(AuditingEntityListener.class) // JPA 엔티티 리스너
public abstract class Timestamped {

    @CreatedDate
    @Column(updatable = false) // updatable = false -> 수정 시 값 변경 방지
    @Temporal(TemporalType.TIMESTAMP) // TIMESTAMP -> 날짜 시간 모두 저장
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime modifiedAt;
}