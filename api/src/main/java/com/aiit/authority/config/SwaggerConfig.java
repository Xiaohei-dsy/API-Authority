package com.aiit.authority.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    // 通过yml依赖控制是否开启swagger
    @Value("${swagger2.enable}")
    private boolean swaggerShow;

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .enable(swaggerShow)
                .select()// 通过.select()方法，去配置扫描接口,RequestHandlerSelectors配置如何扫描接口
                .apis(RequestHandlerSelectors.basePackage("com.aiit.authority.controller"))
                .paths(PathSelectors.any())
                .build()
                .groupName("authority");
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("大数据操作系统")
                .description("对外接口文档")
                .version("1.0")
                .build();
    }
}
