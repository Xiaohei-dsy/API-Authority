package com.aiit.authority.utils;

import com.aiit.authority.BaseTest;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtUtilsTest extends BaseTest {

    @Test
    void createToken() {
        // 生成的token是动态的，相同的两次生成一定是不同的
        String token1 = JwtUtils.createToken("user1");
        try {
            Thread.sleep(500L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String token2 = JwtUtils.createToken("user1");
        Assertions.assertFalse(token1.equals(token2));
    }

    @Test
    void verifyToken() {

        // 验证合法
        String token1 = JwtUtils.createToken("user1");
        assertTrue(JwtUtils.verifyToken(token1, "user1"));

        // 用户非法验证
        assertThrows(JWTVerificationException.class, () -> {
            JwtUtils.verifyToken(token1, "user2");
        });


        // 篡改token签名
        String invalidToken = token1.split("\\.")[0] +
                token1.split("\\.")[1].replaceAll("a", "0") +
                token1.split("\\.")[2];

        // 格式非法验证
        assertThrows(JWTVerificationException.class, () -> {
            JwtUtils.verifyToken(invalidToken, "user1");
        });

        assertThrows(JWTVerificationException.class, () -> {
            JwtUtils.verifyToken(token1 + "a", "user1");
        });

    }

    @Test
    void decodeToken() {
        String token1 = JwtUtils.createToken("user1");
        assertEquals("user1", JwtUtils.decodeToken(token1));
    }
}