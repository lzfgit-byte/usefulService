package com.ilzf.base.config;

import com.ilzf.base.argumentResolvers.RequestBodyJsonArgumentResolvers;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class Config implements WebMvcConfigurer {
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new RequestBodyJsonArgumentResolvers());
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    }
}
