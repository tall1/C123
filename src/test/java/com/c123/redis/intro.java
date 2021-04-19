package com.c123.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class intro {
    public static void main(String[] args) {
        Jedis jedis = new Jedis();
        strings(jedis);

    }

    public static void strings(Jedis jedis) {
        // Strings
        jedis.set("events/city/rome", "32,15,223,828");
        jedis.set("tal", "111");
        jedis.set("dar", "222");

        System.out.println(jedis.get("events/city/rome"));
        System.out.println(jedis.get("tal"));
        System.out.println(jedis.get("dar"));
        jedis.del("dar");
        System.out.println(jedis.get("dar"));

    }

    public static void lists(Jedis jedis) {
        // Lists
        jedis.lpush("queue#tasks", "firstTask");
        jedis.lpush("queue#tasks", "secondTask");

        String task = jedis.rpop("queue#tasks");
    }

    public static void sets(Jedis jedis) {
        // Sets
        jedis.sadd("nicknames", "nickname#1");
        jedis.sadd("nicknames", "nickname#2");
        jedis.sadd("nicknames", "nickname#1");

        Set<String> nicknames = jedis.smembers("nicknames");
        boolean exists = jedis.sismember("nicknames", "nickname#1");

    }

    public static void hashes(Jedis jedis) {
        // Hashes
        jedis.hset("user#1", "name", "Peter");
        jedis.hset("user#1", "job", "politician");

        String name = jedis.hget("user#1", "name");

        Map<String, String> fields = jedis.hgetAll("user#1");
        String job = fields.get("job");
    }

    public static void sortedSets(Jedis jedis) {
        Map<String, Double> scores = new HashMap<>();

        scores.put("PlayerOne", 3000.0);
        scores.put("PlayerTwo", 1500.0);
        scores.put("PlayerThree", 8200.0);

        scores.entrySet().forEach(playerScore -> {
            jedis.zadd("ranking", playerScore.getValue(), playerScore.getKey());
        });

        String player = jedis.zrevrange("ranking", 0, 1).iterator().next();
        long rank = jedis.zrevrank("ranking", "PlayerOne");

        System.out.println("Player name: " + player + " rank: " + rank);
    }

    public static void transactions(Jedis jedis){
        String friendsPrefix = "friends#";
        String userOneId = "4352523";
        String userTwoId = "5552321";

        Transaction t = jedis.multi();
        t.sadd(friendsPrefix + userOneId, userTwoId);
        t.sadd(friendsPrefix + userTwoId, userOneId);
        t.exec();
    }
}
