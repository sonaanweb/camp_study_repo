상품을 판매하는 스토어(`Store`)를 만들고, CRUD를 구현한 다음 필요한 지점에 캐싱을 구현해보자.

1. `Store`는 JPA Entity로서, 다음과 같은 속성을 가지고 있다.
    1. 스토어 ID - Long
    2. 이름 - String
    3. 분류 - String (패션, 디지털 등)
2. 완료 후 각 기능에 적용한 캐싱과 방법, 그리고 그 이유를 정리해보자.


-----------------------------------------------------------------------

### Store Entity 구조와 Multi Cache Configuration 개요
- 상품 판매 Store의 CRUD를 구현한 후, 필요한 위치에 캐싱을 적용하는 실습입니다.
- 실습에서 사용하는 Store Entity는 ID와 이름, 분류만으로 구성된 간단한 구조입니다.
- 목표는 복잡한 비즈니스 로직이 아니라 Entity Caching 연습에 있습니다.
- @CachePut, @CacheEvict, @Cacheable을 통해 데이터 삭제(Delete)까지 포함한 다양한 Cache 동작을 실습합니다.
- Multi Cache Configuration을 추가하여 단일 Cache와 전체 Cache에 대한 설정을 분리하고, 단일 저장 Cache(object)는 TTL을 20초로 짧게 설정하며, 별도의 메서드로 각 Cache 설정을 관리합니다.


### Multi Cache Configuration과 Cache 정책 적용법
- Create 작업 시에는 전체 상점 Cache에 대해 @CacheEvict를 사용하여 새로 만든 상점이 다음 검색에서 바로 나타나도록 기존 Cache를 삭제하도록 설정했습니다.
- 새로 생성된 상점은 첫 번째 Read All에서 Cache되어야 하므로, Create 단계에서는 @CachePut을 추가하지 않고 Read All에서 Cache가 등록되도록 했습니다.
- Individual Cache는 TTI(Time To Idle) 설정을 따로 적용해, 20초 이내에 다시 조회가 있으면 Cache 유지 시간을 재조정하여 자주 조회되는 상점의 Cache가 삭제되지 않도록 했습니다.
- 자주 조회되는 상품(혹은 상점)은 Cache에 계속 유지되기 때문에, 인기가 많거나 라이브 방송 중인 경우 Cache가 빠르게 삭제되지 않도록 분리된 정책을 사용했습니다.
- 상점과 물품 표현을 혼동했으나, 상점에도 동일하게 정책이 적용되어 효율적인 Caching 관리가 가능합니다.


### Create 메서드에 Cache Evict 적용 및 Caching Annotation 관리 방법
- Cache 갱신이 필요할 경우, 단일 Cache는 갱신하고 전체 Cache는 제거하는 방식으로 관리합니다.
- 삭제(Delete) 연산에서는 단일 Cache와 전체 Cache를 모두 삭제하기 위해 추가적인 Caching Annotation이 필요합니다.
- `@CacheEvict`는 `여러 개를 동시에 사용할 수 없기 때문에`, 이럴 경우 `@Caching을 도입`해야 합니다.
- Postman 컬렉션을 활용해 API의 Caching 동작을 테스트할 수 있도록 준비되어 있습니다.


### Read 메서드의 Cache 전략 및 TTL 설정
- @CachePut이 없으면 상품을 추가해도 Cache에 반영되지 않아, 데이터 생성 후 Read All을 호출해야만 Cache에 반영됩니다.
- 개별 상품 및 전체 상품 조회의 Caching이 정상적으로 동작하며, 각각 storecash1(개별)과 store-all-cash(전체)로 Cache에 저장됨을 확인할 수 있습니다.
- Cache 데이터는 JSON 등 읽기 쉬운 형태로 저장되며, 바이트코드 형식으로 저장되는 것도 혼재합니다.
- TTI(Time To Idle) 설정에 따라 일정 시간(예: 20초, 2분) 동안 사용되지 않으면 Cache가 자동 삭제되어 Cache 효율성을 높일 수 있습니다.
- Redis는 TTI를 직접 지원하지 않으므로, GET 요청 시마다 EXPIRE를 새로 갱신하는 방식으로 TTI를 구현할 수 있습니다.


### 상품 업데이트 시 Cache 갱신 전략 및 유의사항
- 상품 정보 업데이트 시 Cache를 갱신하려면 @CachePut을 활용하여 새로운 데이터로 Cache를 갱신합니다.
- `존재하지 않는 데이터를 Cache에 넣고 싶지 않으면 condition 옵션을 활용해야 하지만`, 구현이 복잡하므로 실습에서는 다루지 않습니다.
- 전체 조회 Cache는 업데이트 후 무효화되어야 하므로, 별도 Cache 제거(예: @CacheEvict) 절차를 통해 업데이트 시 전체 Cache도 관리해야 합니다.
- 개발에는 여러 방법이 있으므로, 자신의 상황에 맞춘 적절한 Cache 전략을 선택하고 적용 이유를 명확하게 설명할 수 있으면 됩니다.
- 단일 방법만이 정답은 아니며, 다양한 방법 중 조정과 결정을 통해 최적의 Caching 전략을 결정할 필요가 있습니다.