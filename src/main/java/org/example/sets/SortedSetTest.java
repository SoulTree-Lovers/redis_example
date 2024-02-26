package org.example.sets;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.resps.Tuple;

import java.util.HashMap;
import java.util.List;

public class SortedSetTest {
    public static void main(String[] args) {
        try (JedisPool jedisPool = new JedisPool("127.0.0.1", 6379)) {
            try (Jedis jedis = jedisPool.getResource()) {

                HashMap<String, Double> scores = new HashMap<>();
                scores.put("user1", 100.0);
                scores.put("user2", 93.0);
                scores.put("user3", 85.0);
                scores.put("user4", 10.0);
                scores.put("user5", 50.0);

                jedis.zadd("game1:scores", scores);

                // 키만 출력
                List<String> zrange = jedis.zrange("game1:scores", 0, Long.MAX_VALUE);
                zrange.forEach(System.out::println);

                // 모든 원소 출력
                List<Tuple> tuples = jedis.zrangeWithScores("game1:scores", 0, Long.MAX_VALUE);
                tuples.forEach(it -> System.out.println(it.getElement() + ": "+ it.getScore()));

                // 원소 개수 출력
                System.out.println(jedis.zcard("game1:scores"));

                // 카운트
                double user3Count = jedis.zincrby("game1:scores", 100, "user3");
                System.out.println(user3Count); // 85.0 + 100 => 185.0
            }
        }
    }
}
