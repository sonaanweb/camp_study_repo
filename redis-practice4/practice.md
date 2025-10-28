Spring Security의 Form Login의 세션을 클러스터링 해보자.

1. Spring Security의 Form Login 기능을 구현하고, 로그인 정보가 여러 애플리케이션 인스턴스에 걸쳐서 공유되는 것을 확인해보자.
    1. 편의를 위해 csrf 보안은 해제하고 진행하자.
    2. `UserDetailsService`를 직접 구현하지 않고 `InMemoryUserDetailsManager` 사용해도 괜찮다.

--------------------------------------------------------------------------------------

- 간단한 폼 로그인 UI와 UserDetailsManager로 로그인, 로그아웃 기능을 구현하고 user1, user2 계정 테스트를 진행했습니다.
- 로그인 폼은 Bootstrap을 활용하여 input username, input password로 정보를 post 방식으로 전송하는 구조이며, 로그인 후에는 마이프로필 페이지로 이동합니다.
- Redis 설정에서 직렬화 관련 주석을 해제할 경우, 내부 데이터를 쉽게 읽을 수 있지만 Spring Security의 SecurityContext 객체 직렬화/역직렬화 과정에서 에러가 발생합니다.
- JSON Serializer가 SecurityContext 내부 클래스를 정상적으로 생성자 없이 역직렬화하지 못해 "Cannot construct instance" 에러가 발생합니다.
- SecurityContext를 직접 직렬화·역직렬화하거나, 적절한 생성자 및 filter 구현이 없다면, 현 상태에서는 세션 클래스터링에서 SecurityContext를 JSON 형태로 쓰기 어렵다는 점을 주의해야 합니다.