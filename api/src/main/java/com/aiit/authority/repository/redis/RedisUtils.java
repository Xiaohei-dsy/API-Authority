package com.aiit.authority.repository.redis;

import com.aiit.authority.enums.RedisPreFixEnum;
import com.aiit.authority.enums.ResultCodeEnum;
import com.aiit.authority.exception.RedisException;
import com.aiit.authority.manager.UserManager;
import com.aiit.authority.utils.JedisPoolUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

@Slf4j
@Component
public class RedisUtils {
    /**
     * 过期时长
     */
    // token过期时间
    private static final int HOUR = 60 * 60;
    private static final int MIN = 60;
    private static final int DURATION_TIME = 12 * HOUR;


    @Resource
    private JedisPoolUtils jedisPoolUtils;

    @Resource
    private UserManager userManager;

    public void set(String redisKey, String value) {
        // 在存入redis的时候就顺便设置好过期时间
        try {
            jedisPoolUtils.setEx(redisKey, DURATION_TIME, value);
        } catch (Exception e) {
            throw new RedisException(ResultCodeEnum.REDIS_SET_FAILED);
        }

    }


    public String get(String redisKey) {
        try {
            String redisValue = jedisPoolUtils.get(redisKey);
            return redisValue;
        } catch (Exception e) {
            throw new RedisException(ResultCodeEnum.REDIS_GET_FAILED);
        }
    }

    /**
     * 尝试从redis获取role，如果redis过期则会从MySQL获取并更新redis存储
     *
     * @param username
     * @return
     */
    public int getUserRole(String username) {
        String redisKey = RedisPreFixEnum.ROLE.getPrefix() + username;
        String redisRole = null;
        // 首先尝试从redis获取role
        try {
            redisRole = jedisPoolUtils.get(redisKey);
        } catch (Exception e) {
            throw new RedisException(ResultCodeEnum.REDIS_GET_FAILED);
        }

        // 如果redis为空则读取mysql，并更新redis
        if (redisRole == null) {
            redisRole = userManager.getUser(username).getIsManager().toString();
            jedisPoolUtils.set(redisKey, redisRole);
        }

        return Integer.parseInt(redisRole);
    }

    public String getUsernameFromRedisKey(String redisKey) {
        return redisKey.split("_")[1];
    }


    public void delete(String redisKey) {
        Long success = jedisPoolUtils.del(redisKey);
        if (!Objects.equals(success, 1L)) {
            throw new RedisException(ResultCodeEnum.REDIS_DELETE_FAILED);
        }
    }

    /**
     * 获取key对应value距离过期的剩余秒数
     *
     * @param redisKey
     * @return 返回秒数
     */
    public Long getLeftTime(String redisKey) {
        try {
            return jedisPoolUtils.ttl(redisKey);
        } catch (Exception e) {
            throw new RedisException(ResultCodeEnum.REDIS_GET_LEFT_TIME_FAILED);
        }
    }

    public Integer getDurationTime() {
        return DURATION_TIME;
    }
}