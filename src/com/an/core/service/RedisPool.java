package com.an.core.service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ResourceBundle;

/**
 * Redis 池
 * Created by karas on 2/3/15.
 */
public class RedisPool {

    private static JedisPool pool;

//    private static ShardedJedisPool shardPool;

    //静态代码初始化池配置
    private RedisPool() {
        //加载redis配置文件
        ResourceBundle bundle = ResourceBundle.getBundle("com.an.etc.redis");
        if (bundle == null) {
            throw new IllegalArgumentException("[redis.properties] is not found!");
        }
        //创建jedis池配置实例
        JedisPoolConfig config = new JedisPoolConfig();

        config.setMaxIdle(Integer.valueOf(
                bundle.getString("redis.maxIdle").trim()));
        config.setMaxTotal(Integer.valueOf(
                bundle.getString("redis.maxTotal").trim()));
        config.setMaxWaitMillis(Long.valueOf(
                bundle.getString("redis.maxWaitMillis").trim()));
        config.setTestOnBorrow(Boolean.valueOf(
                bundle.getString("redis.testOnBorrow").trim()));

        pool = new JedisPool(config, bundle.getString("redis.host").trim(),
                Integer.valueOf(bundle.getString("redis.port").trim()));


        //创建多个redis共享服务
//        JedisShardInfo jedisShardInfo1 = new JedisShardInfo(
//                bundle.getString("redis.host"), Integer.valueOf(bundle.getString("redis.port")));

//        JedisShardInfo jedisShardInfo2 = new JedisShardInfo(
//                bundle.getString("redis2.host"), Integer.valueOf(bundle.getString("redis.port")));

//        List<JedisShardInfo> list = new LinkedList<>();
//        list.add(jedisShardInfo1);
////      list.add(jedisShardInfo2);
//
//        //根据配置文件,创建shared池实例
//        shardPool = new ShardedJedisPool(config, list);
    }


    public static Jedis getJedis() {
        try {
            if (pool == null) {
                new RedisPool();
            }
            return pool.getResource();
        } catch (Exception e) {
            return null;
        }
    }

//    public static ShardedJedis getShardedJedis() {
//        try {
//            if (shardPool == null) {
//                new RedisPool();
//            }
//            return shardPool.getResource();
//        } catch (Exception e) {
//            return null;
//        }
//    }

    public static void returnBrokenResource(Jedis jedis) {
        pool.returnBrokenResource(jedis);
    }

    public static void returnResource(Jedis jedis) {
        pool.returnResource(jedis);
    }

}
