package com.zjx.youchat.chat.config;

import com.zjx.youchat.chat.controller.TokenInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    private final StringRedisTemplate redisTemplate;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenInterceptor(redisTemplate))
                .excludePathPatterns("/user/captcha", "/user/register", "/user/login")
                .excludePathPatterns("/static/**").excludePathPatterns("/favicon.ico");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:/favicon.ico");
    }
}
