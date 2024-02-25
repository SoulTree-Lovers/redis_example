package org.example.strings;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

import java.util.List;

public class StringTest {
    public static void main(String[] args) {
        try (var jedisPool = new JedisPool("127.0.0.1", 6379)) {
            try (Jedis jedis = jedisPool.getResource()) {
                // 데이터 키-값으로 저장
                jedis.set("users:300:email", "kang@naver.com");
                jedis.set("users:300:name", "kang");
                jedis.set("users:300:age", "25");

                // 한 개 가져오기
                var result = jedis.get("users:300:email");
                System.out.println(result);

                // 여러 개 가져오기
                var results = jedis.mget("users:300:email", "users:300:name", "users:300:age");
                results.forEach(System.out::println);

                // 카운터 증가하기
                long count = jedis.incr("count");
                System.out.println(count);

                // 파이프라인으로 여러 개의 명령어를 한 번에 내리기
                Pipeline pipelined = jedis.pipelined();
                pipelined.set("users:400:email", "ksm@gmail.com");
                pipelined.set("users:400:name", "ksm");
                pipelined.set("users:400:age", "25");
                List<Object> objects = pipelined.syncAndReturnAll();

                objects.forEach(it -> System.out.println(it.toString()));
            }
        }
    }
}
