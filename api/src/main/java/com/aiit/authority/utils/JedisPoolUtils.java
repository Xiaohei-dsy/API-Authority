package com.aiit.authority.utils;

import com.aiit.authority.config.JedisConfig;
import com.aiit.authority.enums.ResultCodeEnum;
import com.aiit.authority.exception.RedisException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.function.Function;

@Slf4j
@Service
public class JedisPoolUtils implements RedisPool {

    @Resource
    private JedisConfig jedisConfig;

    private static JedisPool jedisPool;

    @PostConstruct
    public void init() {
        log.info("初始化 jedis 连接池工具");
        jedisPool = jedisConfig.redisPoolFactory();
    }


    /**
     * JedisPool通用执行方法
     *
     * @param function 需要执行的方法
     * @param <T>      返回结果类型
     * @return 返回结果
     */
    private <T> T execute(Function<Jedis, T> function) {
        // try-with-resource
        // 每次调用返回连接池资源
        try (Jedis jedis = jedisPool.getResource()) {
            // 将jedis资源应用到function中
            return function.apply(jedis);
        } catch (Exception e) {
            log.error("redis pool error", e);
            throw new RedisException(ResultCodeEnum.REDIS_POOL_FAILED);
        }
    }


    @Override
    public String get(String key) {
        return execute(jedis -> jedis.get(key));
    }

    @Override
    public String set(String key, String value) {
        return execute(jedis -> jedis.set(key, value));
    }

    @Override
    public String setEx(String key, Integer time, String value) {
        return execute(jedis -> jedis.setex(key, time, value));
    }


    @Override
    public Long del(String key) {
        return execute(jedis -> jedis.del(key));
    }

    @Override
    public Long del(String... keys) {
        return execute(jedis -> jedis.del(keys));
    }

    @Override
    public Boolean exists(String key) {
        return execute(jedis -> jedis.exists(key));
    }

    @Override
    public Long expire(String key, Integer value) {
        return execute(jedis -> jedis.expire(key, value));
    }

    @Override
    public Long ttl(String key) {
        return execute(jedis -> jedis.ttl(key));
    }

}
