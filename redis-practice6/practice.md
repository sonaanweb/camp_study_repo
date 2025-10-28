특가 물량, 라이브 등 특정 상황에서는 짧은 시간동안 물품이 엄청나게 많이 팔린다!

1. 마지막에 만든 Cache 프로젝트에 리더보드 기능에서 사용한 `ItemOrder`를 추가해보자. (복습)
    1. 구매된 `Item`들의 구매 순위를 Sorted Set으로 관리하자.
    2. 가장 많이 구매된 `Item` 10개를 조회하는 기능을 만들어보자.
2. `ItemOrder`에 대하여 Write-Behind 캐싱을 구현해보자.
    1. Write-Behind는 Annotation 기반으로 구현할 수 없다.
    2. `RestTemplate`등을 통하여 수동으로 Redis에 저장하고, 조회도 Redis에서 진행하자.
    3. 이후 일정 시간바다 Redis의 데이터를 데이터베이스로 옮기는 기능을 만들어보자.

---------------------------------------------------------------------------------

### Write Behind Caching 구현 및 설정 요점
- Write Behind Cache는 Annotation 방식으로 만들기 어렵기 때문에 직접 구현이 필요합니다.
- Item Order에서 Write Behind Caching을 위해 REST Template과 Scheduler를 활용하며, 특별한 설정 없이 Spring Boot 기본 설정과 Enable Scheduling만 추가했습니다.
- Cache 및 Scheduler 설정은 관련성이 높아 Cache Config에 함께 두는 것이 적절하며, 기능에 따라 Config의 분리도 고려할 수 있습니다.
- Redis를 사용하여 Rank Template과 Order Template을 추가하고, Item Order DTO 관리를 위한 ItemOrderDto 형태 및 Java Serializer를 사용했습니다.
- Item Order Entity에는 Item ID Column과 Join 설정(insert, update false)을 추가해서, ID만으로도 관계를 형성할 수 있도록 했고, 이는 DTO 및 Entity 정보 중복 방지 목적입니다.

### Write Behind Cache 구현과 Redis 활용 정리
- Write Behind Cache는 주문 정보를 List(Queue)로 저장하며, 들어온 순서대로 처리하기 위해 List Operation을 사용합니다.
- 주문 처리 중에 중복 저장이나 충돌 방지를 위해 Key의 이름을 변경(Rename)하여 더 이상 데이터를 받지 않는 List와 신규 List로 분리합니다.
- Scheduler가 주기적으로 List의 주문 데이터를 꺼내어 Database에 한 번에 저장하며, 이 과정에서 Stream과 Map 활용, Delete 처리로 효율을 높입니다.
- Accepted 응답은 요청이 바로 처리되는 것이 아니라 서버가 의도를 인지하고 처리 예정임을 의미하며, 일시적으로 Cache에 저장 후 나중에 DB에 반영되는 구조입니다.
- Redis Template을 사용하여 데이터를 임시 저장했다가 일괄적으로 DB에 반영하는 방식은, 대용량 처리와 성능 향상에 적합하며, JPA의 saveAll이 save보다 성능이 좋다는 점도 함께 고려해야 합니다.
- Write Behind Cache는 Annotation 기반 자동화가 어렵지만, 직접 구현을 통해 세련된 처리가 가능하며 그 외 Track & Section 등의 고급 주제는 향후 학습이 필요합니다.
