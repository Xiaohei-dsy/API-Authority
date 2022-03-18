package com.aiit.authority.config;

import com.aiit.authority.interceptor.AdminInterceptor;
import com.aiit.authority.interceptor.TokenInterceptor;
import com.aiit.authority.interceptor.TraceIdInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 登录验证token拦截器的配置类
 */

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Bean
    public TokenInterceptor loginInterceptor() {
        return new TokenInterceptor();
    }

    @Bean
    public AdminInterceptor adminInterceptor() {
        return new AdminInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        /**
         * 注册traceid拦截器
         */
        registry.addInterceptor(new TraceIdInterceptor());

        /**
         * 注册Login拦截器并设置路径匹配:
         * 除登录 登出 注册 及swagger相关接口外所有接口均需要验证token以保证请求合法性
         * token有效期内可免登录作业，token过期需要再次登录
         */
        registry.addInterceptor(loginInterceptor())
                .excludePathPatterns("/**/login")
                // logout比较特殊，虽然不走拦截器但仍需token
                .excludePathPatterns("/**/logout")
                .excludePathPatterns("/**/register")
                // 查询用户是否存在的操作发生在登陆之前，所以不需要token
                .excludePathPatterns("/**/exist")
                // 对外暴露token接口，不拦截
                .excludePathPatterns("/**/token/**")
                // swagger相关，不拦截
                .excludePathPatterns("/null")
                .excludePathPatterns("/swagger-ui.html")
                .excludePathPatterns("/swagger-resources/**")
                .excludePathPatterns("/configuration/security")
                .excludePathPatterns("/v2/api-docs")
                .excludePathPatterns("/error")
                .excludePathPatterns("/webjars/**")
                .excludePathPatterns("/**/favicon.ico")
                .addPathPatterns("/**");

        /**
         * 注册admin拦截器并设置路径匹配:
         * 为防止user越级调用admin相关接口，为每个admin接口绑定身份验证
         * login拦截器验证token合法后，会进一步根据token携带的username验证该请求的发出者是否为管理员
         * 如果检测到普通用户越级调用管理员接口会返回给前端对应信息
         * 建议之后任何需要管理员权限的接口全部在route内携带 “/admin/”以匹配该拦截器
         */
        registry.addInterceptor(adminInterceptor())
                .excludePathPatterns("/**/login")
                .excludePathPatterns("/**/logout")
                .excludePathPatterns("/**/register")
                .addPathPatterns("/**/admin/**");
    }
}
