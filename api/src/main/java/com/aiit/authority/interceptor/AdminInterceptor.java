package com.aiit.authority.interceptor;

import com.aiit.authority.enums.ResultCodeEnum;
import com.aiit.authority.enums.UserPositionEnum;
import com.aiit.authority.exception.AuthenticateException;
import com.aiit.authority.repository.redis.RedisUtils;
import com.aiit.authority.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 本拦截器在调用admin相关接口之前，校验调用者身份是否为admin，避免出现user越级调用admin接口的情况
 */

@Slf4j
public class AdminInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        /**
         * 在本拦截器执行之前，login拦截器应该已经验证过token合法性，所以无需重复验证
         */

        // 获取token并解析出username
        String username = tokenService.decodeToken(request.getHeader("Authorization"));

        // 以username查询缓存，如果查到的role不是管理员则抛出异常
        if (redisUtils.getUserRole(username) != UserPositionEnum.ADMIN.getValue()) {
            throw new AuthenticateException(ResultCodeEnum.WRONG_USER_ROLE);
        }

        log.info("访问者权限校验通过");
        return true;
    }
}
