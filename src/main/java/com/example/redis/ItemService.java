package com.example.redis;

import com.example.redis.domain.Item;
import com.example.redis.domain.ItemDto;
import com.example.redis.repo.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
public class ItemService {
    private final ItemRepository itemRepository;
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    /**
     * [조회]
     * 이 메서드의 결과는 캐싱이 가능하다 = Cacheable
     * cacheNames: 적용할 캐시 규칙을 지정하기 위한 이름
     * -> 즉, 이 메서드로 인해 만들어질 캐시를 지칭하는 이름
     * key: 캐시 데이터를 '구분'하기 위해 활용하는 값 ex) itemCache::1...2..
     *
     * (전략 : Cache-aside)
     * 동작 방식 -> 첫번째 조회는 원본 DB 접근, 이후 접근은 캐시에 있으면 캐시에 접근 원본 DB까지 안 감
     * return 데이터를 캐시로 만들어주는 것
     */
    @Cacheable(cacheNames = "itemCache", key = "args[0]")
    public ItemDto readOne(Long id) {
        log.info("Read One: {}", id);
        return itemRepository.findById(id)
                .map(ItemDto::fromEntity)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    /**
     * [전체 조회]
     */
    @Cacheable(cacheNames = "itemAllCache", key = "methodName")
    public List<ItemDto> readAll() {
        return itemRepository.findAll()
                .stream()
                .map(ItemDto::fromEntity)
                .toList();
    }


    /**
     * [쓰기]
     * Cacheput: 항상 메서드를 실행하고, 결과를 캐싱한다.
     * result.id: 메서드의 결과(return)이 가지고 있는 id
     *
     * Redis에 저장하는 데이터의 key가 cacheName::key의 형태로 저장되기 때문에,
     * readOne이 찾는 cacheName::key도 동일한 Key를 찾아내서, Redis의 데이터를 활용할 수 있도록 활용된다
     *
     * (전략: 생성, 수정에 적용하면 Write-Through)
     */
    @CachePut(cacheNames = "itemCache", key = "#result.id")
    public ItemDto create(ItemDto dto) {
        return ItemDto.fromEntity(itemRepository.save(Item.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .build()));
    }

    /**
     * [수정]
     * CachePut + CacheEvict
     * CacheEvict: update가 일어나면 readAll에 있는 캐시를 지워주고 싶다.
     * 아이템의 정보가 바뀌었으니,
     * 데이터를 전부 돌려준 결과가 더이상 유효하지 않다고 이야기 하는거라 생각할 수 있다.
     *
     * key = "#id" 로도 쓸 수 있다
     */
    @CachePut(cacheNames = "itemCache", key = "args[0]")
    @CacheEvict(cacheNames = "itemAllCache", allEntries = true)
    public ItemDto update(Long id, ItemDto dto) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setPrice(dto.getPrice());
        return ItemDto.fromEntity(itemRepository.save(item));
    }


    /**
     * [삭제]
     * DB에서 데이터를 지우기 때문에 Redis에서도 해당 데이터는 더이상 유효하지 않아야 한다.
     * 단일 + 전체 조회 캐시 무효화
     * CacheEvict
     * itemCache, itemAllCache
     */
    @CacheEvict(cacheNames = {"itemCache", "itemAllCache"}, key= "#id", allEntries = true)
    public void delete(Long id) {
        itemRepository.deleteById(id);
        log.info("Deleted item: {}", id);
    }


    /**
     * 검색
     *
     * args[0] 첫번째 인자 검색어 - q
     * args[1] 두번째 인자 page 번호, pageSize 페이지 크기
     * itemSearchCache::["apple", 1, 5]
     */
    @Cacheable(cacheNames = "itemSearchCache",
            key = "{args[0], args[1].pageNumber, args[1].pageSize}")
    public Page<ItemDto> searchByName(String query, Pageable pageable){
        return itemRepository.findAllByNameContains(query, pageable)
                .map(ItemDto::fromEntity);
    }

}
