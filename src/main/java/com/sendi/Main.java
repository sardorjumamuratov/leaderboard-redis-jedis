package com.sendi;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPooled;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        try (JedisPooled jedis = new JedisPooled("localhost", 6379, "default", "sendi8240")) {
            // Store & Retrieve a simple string
            jedis.set("foo", "bar");
            System.out.println(jedis.get("foo")); // prints bar

            // Store & Retrieve a HashMap
            Map<String, String> hash = new HashMap<>();

            hash.put("name", "John");
            hash.put("surname", "Smith");
            hash.put("company", "Redis");
            hash.put("age", "29");
            jedis.hset("user-session:123", hash);

            System.out.println(jedis.hgetAll("user-session:123"));

            ArrayList<String> names = new ArrayList<>(List.of("Liam", "Noah", "Oliver", "Elijah", "William",
                    "James", "Benjamin", "Lucas", "Henry", "Alexander",
                    "Mason", "Michael", "Ethan", "Daniel", "Jacob",
                    "Logan", "JacksonJack", "Owen", "Theodore", "Aiden", "Samuel",
                    "Luke", "Asher", "Carter", "Julian", "Anthony", "Hudson", "Adrian", "Aaron", "Eli", "Landon", "Jonathan", "Nolan",
                    "Cooper", "Brayden", "Nicholas", "Ezekiel", "Jose"));

            Random random = new Random();

            for (long i = 0; i < 1_000_000L; i++) {
                String name = names.get(random.nextInt(names.size())) + i;
                jedis.zadd("leaderboard", random.nextInt(10, 1000), name);
            }

            jedis.zadd("leaderboard", 1001.00, "Sardor");
            jedis.zadd("leaderboard", 1010.00, "Max");

            System.out.println(jedis.zrevrange("leaderboard", 0, -1));
            System.out.println(jedis.zrank("leaderboard", "Sardor"));
        }
    }
}