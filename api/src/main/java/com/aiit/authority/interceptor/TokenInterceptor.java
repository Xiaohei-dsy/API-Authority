package com.aiit.authority.interceptor;

import com.aiit.authority.enums.RedisPreFixEnum;
import com.aiit.authority.enums.ResultCodeEnum;
import com.aiit.authority.exception.AuthenticateException;
import com.aiit.authority.repository.redis.RedisUtils;
import com.aiit.authority.service.TokenService;
import com.aiit.authority.service.dto.TokenInfoDTO;
import com.aiit.authority.utils.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class TokenInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private TokenService tokenService;

    @Override
    /**
     * 只有返回true时对应的route才会成功执行
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {

        // 1. 拿到token判空
        if (request.getHeader("Authorization") == null) {
            log.error("被拦截的路由为：{}", request.getRequestURI());
            throw new AuthenticateException(ResultCodeEnum.NO_TOKEN_IN_REQUEST);
        }

        // 2. 调用服务，基于token验证用户身份
        TokenInfoDTO tokenInfoDTO = tokenService.validateToken(request.getHeader("Authorization"));
        if (!tokenInfoDTO.getValid()) {
            throw new AuthenticateException(ResultCodeEnum.IDENTITY_VERIFICATION_FAILED);
        }

        // 3. 确认token合法后，将username存储在ThreadLocal
        ThreadLocalUtil.set("username", tokenInfoDTO.getUsername());

        // 4. 判断是否需要续期，只有剩余时间<25%有效期时才会续签
        String redisTokenKey = RedisPreFixEnum.TOKEN.getPrefix() + tokenInfoDTO.getUsername();
        if (redisUtils.getLeftTime(redisTokenKey) < redisUtils.getDurationTime() / 4) {
            // 重新向redis写入即可
            redisUtils.set(redisTokenKey, tokenInfoDTO.getRawToken());
            log.info("用户 {} 在有效期内免密登录，token成功续签", tokenInfoDTO.getUsername());
        }

        log.info("用户登录校验通过");
        return true;
    }

    /**
     * postHandle在handler方法成功执行之后、dispatcher渲染视图之前执行。
     * 如果任何一个拦截器的preHandle方法返回false或者抛出异常，或者handler方法中抛出异常都不会执行postHandle方法
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    /**
     * afterCompletion方法在视图渲染结束后执行，并且无论handler方法中是否抛出异常都一定会执行
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
        /**
         * 由于servlet采用了线程池，一旦线程复用会出现ThreadLocal串用的现象，所以每次用完都要在这里清除一下
         */
        ThreadLocalUtil.remove("username");
        log.info("请求完成，清除线程本地存储");
    }
}
