package org.example.bitmap;

import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.args.GeoUnit;
import redis.clients.jedis.params.GeoSearchParam;
import redis.clients.jedis.resps.GeoRadiusResponse;

import java.util.List;
import java.util.stream.IntStream;

public class BitmapTest {
    public static void main(String[] args) {
        try (JedisPool jedisPool = new JedisPool("127.0.0.1", 6379)) {
            try (Jedis jedis = jedisPool.getResource()) {

                // 100, 200, 300번째 비트를 1로 설정
                jedis.setbit("bitmap", 100, true);
                jedis.setbit("bitmap", 200, true);
                jedis.setbit("bitmap", 300, true);

                // bit 가져오기
                boolean bit1 = jedis.getbit("bitmap", 100);
                boolean bit2 = jedis.getbit("bitmap", 500);
                System.out.println(bit1); // true
                System.out.println(bit2); // false

                // 1인 bit 개수 세기
                long bitcount = jedis.bitcount("bitmap");
                System.out.println(bitcount); // 3

                // 파이프라인을 사용하여 데이터 세팅
                Pipeline pipelined = jedis.pipelined();
                IntStream.rangeClosed(0, 1000000).forEach(it -> {
                    pipelined.sadd("set:test", String.valueOf(it), "1"); // 메모리 사용량: 40388792
                    pipelined.setbit("bitmap:test", it, true); // 메모리 사용량: 131128

                    if (it == 1000) {
                        pipelined.sync(); // 1000개 단위로 파이프라인 싱크
                    }
                });
                pipelined.sync();

                // 파이프라인 없이 세팅 --> 굉장히 오래 걸림
                IntStream.rangeClosed(0, 1000000).forEach(it -> {
                    jedis.sadd("set:test", String.valueOf(it), "1");
                    jedis.setbit("bitmap:test", it, true);
                });
            }
        }
    }
}
