package com.an.core.mybatis.cache;

import com.an.core.service.RedisPool;
import org.apache.ibatis.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RedisCache implements Cache {

    private static Logger logger = LoggerFactory.getLogger(RedisCache.class);
    /**
     * The ReadWriteLock.
     */
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private String id;

    public RedisCache(final String id) {
        if (id == null) {
            throw new IllegalArgumentException("Cache instances require an ID");
        }
        logger.debug("RedisCache:" + id);
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public int getSize() {
        Jedis jedis = null;
        int result = 0;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = RedisPool.getJedis();
            result = Integer.valueOf(jedis.dbSize().toString());
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null)
                RedisPool.returnBrokenResource(jedis);
        } finally {
            if (borrowOrOprSuccess)
                RedisPool.returnResource(jedis);
        }
        return result;

    }

    @Override
    public void putObject(Object key, Object value) {

        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = RedisPool.getJedis();
            jedis.set(SerializeUtil.serialize(key.hashCode()), SerializeUtil.serialize(value));
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null)
                RedisPool.returnBrokenResource(jedis);
        } finally {
            if (borrowOrOprSuccess)
                RedisPool.returnResource(jedis);
        }

    }

    @Override
    public Object getObject(Object key) {
        Jedis jedis = null;
        Object value = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = RedisPool.getJedis();
            value = SerializeUtil.unserialize(jedis.get(SerializeUtil.serialize(key.hashCode())));
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null)
                RedisPool.returnBrokenResource(jedis);
        } finally {
            if (borrowOrOprSuccess)
                RedisPool.returnResource(jedis);
        }
        return value;
    }

    @Override
    public Object removeObject(Object key) {
        Jedis jedis = null;
        Object value = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = RedisPool.getJedis();
            value = jedis.expire(SerializeUtil.serialize(key.hashCode()), 0);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null)
                RedisPool.returnBrokenResource(jedis);
        } finally {
            if (borrowOrOprSuccess)
                RedisPool.returnResource(jedis);
        }
        return value;
    }

    @Override
    public void clear() {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = RedisPool.getJedis();
            jedis.flushDB();
            jedis.flushAll();
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null)
                RedisPool.returnBrokenResource(jedis);
        } finally {
            if (borrowOrOprSuccess)
                RedisPool.returnResource(jedis);
        }
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }


}

