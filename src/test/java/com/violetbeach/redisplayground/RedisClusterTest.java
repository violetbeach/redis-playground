package com.violetbeach.redisplayground;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RedisClusterTest {

    @Autowired
    RedisTemplate redisTemplate;

    String dummyValue = "banana";

    @Test
    void setValues() {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();

        // 수동으로 Master 노드 exit

        for(int i = 0; i < 1000; i++) {
            String key = String.format("name:%d", i); // name:{i}
            ops.set(key, dummyValue);;
        }
    }

    @Test
    void getValues() {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();

        for(int i = 0; i < 1000; i++) {
            String key = String.format("name:%d", i);
            String value = ops.get(key);

            /*
            * Master가 죽었지만 slave가 승격했다면 데이터가 삽입
            *
            * yml은 그대로지만, Spring Redis 라이브러리로 인해서 자동으로 새로 승격한 Master 노드에서 데이터를 찾게됨
            * */
            assertEquals(value, dummyValue);
        }
    }

}
