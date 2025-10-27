package com.example.redis.repo;

import com.example.redis.domain.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
    // 페이징 처리 - 이름을 기준으로 검색
    Page<Item> findAllByNameContains(String name, Pageable pageable);
}
