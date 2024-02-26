package org.example.geospatial;

import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.args.GeoUnit;
import redis.clients.jedis.params.GeoSearchParam;
import redis.clients.jedis.resps.GeoRadiusResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeospatialTest {
    public static void main(String[] args) {
        try (JedisPool jedisPool = new JedisPool("127.0.0.1", 6379)) {
            try (Jedis jedis = jedisPool.getResource()) {
                // geoadd
                jedis.geoadd("stores:geo", 127.02133124, 37.4942131123, "home");
                jedis.geoadd("stores:geo", 127.02324342, 37.4952412312, "school");

                // geodist
                Double distance = jedis.geodist("stores:geo", "home", "school");
                System.out.println(distance + "m"); // m 단위

                // geosearch
                // 반경 600미터 내에 있는 좌표 가져오기
                List<GeoRadiusResponse> responses = jedis.geosearch(
                        "stores:geo",
                        new GeoCoordinate(127.027, 37.491),
                        600,
                        GeoUnit.M // 거리 단위
                );

                System.out.println(responses);

                // 좌표 등의 정보를 가져오기 위한 방법
                List<GeoRadiusResponse> responses2 = jedis.geosearch("stores:geo",
                        new GeoSearchParam()
                                .fromLonLat(new GeoCoordinate(127.027, 37.491))
                                .byRadius(600, GeoUnit.M)
                                .withCoord()
                );

                responses2.forEach(it -> System.out.printf("%s %f %f", it.getMemberByString(), it.getCoordinate().getLongitude(), it.getCoordinate().getLatitude()));
            }
        }
    }
}
