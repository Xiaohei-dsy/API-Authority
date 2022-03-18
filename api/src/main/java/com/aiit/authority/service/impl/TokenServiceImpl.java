package com.aiit.authority.service.impl;

import com.aiit.authority.enums.RedisPreFixEnum;
import com.aiit.authority.repository.redis.RedisUtils;
import com.aiit.authority.service.TokenService;
import com.aiit.authority.service.dto.TokenInfoDTO;
import com.aiit.authority.utils.JwtUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TokenServiceImpl implements TokenService {

    @Resource
    private RedisUtils redisUtils;


    @Override
    public TokenInfoDTO validateToken(String bearerToken) {

        String token = null;
        String jwtUsername = null;
        boolean isValid = true;

        try {
            // 2. 验证Bearer前缀，并返回token本身
            token = JwtUtils.getTokenWithoutPrefix(bearerToken);

            // 3. 解析token包含的信息(payload部分采用base64加密，无需密钥即可解密)
            jwtUsername = JwtUtils.decodeToken(token);

            // 4. 验证jwt是否合法(是否过期，payload是否被篡改)，如果非法会抛出对应异常，由全局handler处理
            JwtUtils.verifyToken(token, jwtUsername);

            // 5. 结合redis缓存判断token在S端是否还有效
            String redisTokenKey = RedisPreFixEnum.TOKEN.getPrefix() + jwtUsername;
            // 用户登出和token过期都会在redis中自动删除记录
            if (redisUtils.get(redisTokenKey) == null) {
                isValid = false;
            }
            // 确认前端发来的token和redis中存储的一致，避免有人从C端劫取旧token去登录
            else if (!redisUtils.get(redisTokenKey).equals(token)) {
                isValid = false;
            }
        } catch (Exception e) {
            isValid = false;
        }

        // 构建dto回传信息
        TokenInfoDTO tokenInfoDTO = new TokenInfoDTO();
        tokenInfoDTO.setRawToken(token);
        tokenInfoDTO.setUsername(jwtUsername);
        tokenInfoDTO.setValid(isValid);

        return tokenInfoDTO;
    }

    @Override
    public String decodeToken(String bearerToken) {

        // 2. 验证Bearer前缀，并返回token本身
        String token = JwtUtils.getTokenWithoutPrefix(bearerToken);

        // 3. 解析token包含的信息(payload部分采用base64加密，无需密钥即可解密)
        return JwtUtils.decodeToken(token);

    }
}
