package org.example.lists;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

public class ListTest {
    public static void main(String[] args) {

        try (JedisPool jedisPool = new JedisPool("127.0.0.1", 6379)) {
            try (Jedis jedis = jedisPool.getResource()) {
                // list
                // 1. stack (한쪽에 넣고, 넣은쪽부터 빼기)
                jedis.rpush("stack1", "aa");
                jedis.rpush("stack1", "bb");
                jedis.rpush("stack1", "cc");

//                List<String> stack1 = jedis.lrange("stack1", 0, -1);
//                stack1.forEach(System.out::println);

                System.out.println(jedis.rpop("stack1"));
                System.out.println(jedis.rpop("stack1"));
                System.out.println(jedis.rpop("stack1"));

                // 2. queue (한쪽에 넣고, 반대쪽부터 빼기)
                jedis.rpush("queue1", "hello1");
                jedis.rpush("queue1", "hello2");
                jedis.rpush("queue1", "hello3");

                System.out.println(jedis.lpop("queue1"));
                System.out.println(jedis.lpop("queue1"));
                System.out.println(jedis.lpop("queue1"));

                // 3. block: 일정 시간 동안 pop하기를 기다림
                List<String> blpop = jedis.blpop(30, "queue:block");
                if (blpop != null) {
                    blpop.forEach(System.out::println);
                }
            }
        }
    }
}
