package com.zilean.queue.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static com.zilean.queue.constant.ZileanConstant.CLIENT_PATH_PATTERN;

/**
 * 描述:
 *
 * @author xjh
 * created on 2019-07-05 19:38
 */
@Configuration
public class DefaultWebMvcConfigurer implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenHandleInterceptor()).addPathPatterns(CLIENT_PATH_PATTERN);
    }
}
