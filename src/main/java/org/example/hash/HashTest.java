package org.example.hash;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HashTest {
    public static void main(String[] args) {

        try (JedisPool jedisPool = new JedisPool("127.0.0.1", 6379)) {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.hset("users:100:info", "name", "kang"); // 키-값 하나 넣기

                // 여러 개의 값 한 번에 넣기
                Map<String, String> users = new HashMap<>();
                users.put("email", "kang@naver.com");
                users.put("age", "25");

                jedis.hset("users:100:info", users);

                System.out.println(jedis.hgetAll("users:100:info")); // 값 모두 출력

                jedis.hdel("users:100:info", "email"); // 값 지우기

                System.out.println(jedis.hgetAll("users:100:info")); // 값 모두 출력

                System.out.println(jedis.hget("users:100:info", "name")); // 값 하나 출력

                // hincr: 카운트
                jedis.hincrBy("users:100:info", "counter", 10);
                System.out.println(jedis.hget("users:100:info", "counter"));
            }
        }


    }
}
