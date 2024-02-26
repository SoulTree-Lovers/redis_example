package org.example.sets;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

public class SetTest {
    public static void main(String[] args) {

        try (JedisPool jedisPool = new JedisPool("127.0.0.1", 6379)) {
            try (Jedis jedis = jedisPool.getResource()) {

                jedis.sadd("users:500:follow", "100", "200", "300"); // 값 넣기
                jedis.srem("users:500:follow", "200"); // 값 지우기

                System.out.println(jedis.smembers("users:500:follow")); // 모든 값 출력
                System.out.println(jedis.sismember("users:500:follow", "100")); // true
                System.out.println(jedis.sismember("users:500:follow", "200")); // false
                System.out.println(jedis.scard("users:500:follow")); // 원소 개수 출력

                jedis.sadd("users:400:follow", "100", "200", "300"); // 값 넣기
                System.out.println(jedis.sinter("users:500:follow", "users:400:follow")); // 교집합 출력
            }
        }
    }
}
