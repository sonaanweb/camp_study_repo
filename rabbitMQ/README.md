<img width="500" height="700" alt="image" src="https://github.com/user-attachments/assets/2e148b6c-d6b5-4504-b418-15d5867af386" />

1. 외부에서 order 요청을 엔드포인트로 받음
2. 요청이 오면 order에서 메세지를 만듦 (큐에 보낼 메세지)
3. 두 개의 큐에 각각 메세지를 넣어준다 (product, payment)
4. 들어간 메시지를 뒤쪽에 연결된 컨슈머에서 가져가서 소비하게 될 것 <br>
⭐ product 두개는 동일한 기능이다.
정리하자면 이 프로젝트에서는 프로듀서 1개(Order), 컨슈머(Product 1, Product 2, Payment) 3개로 운영된다.

👉🏻 <'market'> = exchange

👉🏻 order -> <'market'> --`binding`--> queue (`market.proudct / market.payment`) -> consumer (3개)
