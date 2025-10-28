package com.sparta.springauth;

import com.sparta.springauth.food.Food;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BeanTest {

    /**
     * 기본적으로 Food 타입의 빈을 먼저 찾음 -> 두개네? -> 뭐 넣어야 해?
     * Food food; -> error : bean 객체가 하나 이상. 명시해줘야 함
     * (+)
     * 컴포넌트에
     * @Primary -> 두개 이상이어도 설정된 bean 객체 주입
     * @Qualifier -> 해당 bean 객체 주입
     * 동시 적용 시 @qualifier 우선순위가 더 높다
     * 같은 타입의 bean이 여러 개 있을 때는 범용적 bean 객체에서는 primary
     * 지엽적 사용 bean 객체에서는 qualifier 사용
     */
    @Autowired
    Food chicken;

    @Autowired
    Food pizza;

    @Test
    @DisplayName("테스트")
    void test1(){
        pizza.eat();
        chicken.eat();
    }
}
