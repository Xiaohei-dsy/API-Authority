package com.aiit.authority.utils;

import com.aiit.authority.BaseTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import javax.annotation.Resource;

@Slf4j
public class JedisUtilsTest extends BaseTest {

    @Resource
    private JedisPoolUtils jedisPoolUtils;

    public static final String TEST = "test:%s";

    @Test
    public void testRedisCrud() {
        String key = "ky";
        String value = "sv";
        String formatKey = String.format(TEST, key);
        jedisPoolUtils.set(formatKey, value);

        // get set 正常
        Assertions.assertEquals(jedisPoolUtils.get(formatKey), value);

        // key 存在正常
        Assertions.assertTrue(jedisPoolUtils.exists(formatKey));
        Assertions.assertFalse(jedisPoolUtils.exists(key));

        // 特殊的过期时间
        Assertions.assertEquals(jedisPoolUtils.ttl(key), -2);
        Assertions.assertEquals(jedisPoolUtils.ttl(formatKey), -1);

        jedisPoolUtils.del(formatKey);
        Assertions.assertNull(jedisPoolUtils.get(formatKey));

        Assertions.assertEquals(jedisPoolUtils.ttl(formatKey), -2);
    }

    @Test
    public void testSetEx() {
        String key = String.format(TEST, "key");
        String value = "sv";
        int time = 3;
        jedisPoolUtils.setEx(key, time, value);
        Assertions.assertEquals(jedisPoolUtils.ttl(key), time);
        Assertions.assertEquals(jedisPoolUtils.get(key), value);
        try {
            Thread.sleep(3000L);
        } catch (Exception e) {
            log.error("暂停3s 失败", e);
        }

        Assertions.assertNull(jedisPoolUtils.get(key));
        Assertions.assertEquals(jedisPoolUtils.ttl(key), -2);
    }


}
