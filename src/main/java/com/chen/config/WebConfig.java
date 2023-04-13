package com.chen.config;

import com.chen.filters.SetSystemNameFilter;
import com.chen.interceptors.IndexInterceptor;
import com.chen.interceptors.RefreshTokenInterceptor;
import com.chen.interceptors.UserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @Author @Chenxc
 * @Date 2023/3/14 18:23
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private SetSystemNameFilter filter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RefreshTokenInterceptor(redisTemplate)).addPathPatterns("/**").excludePathPatterns("/admin/login","/admin/register","/admin/captcha","/static/**");
        registry.addInterceptor(new IndexInterceptor()).addPathPatterns("/admin/**").excludePathPatterns("/admin/login","/admin/register","/admin/captcha","/static/**");
        registry.addInterceptor(new UserInterceptor()).addPathPatterns("/user/**");
       }

    @Bean
    public FilterRegistrationBean<SetSystemNameFilter> filterRegistrationBean(){
        FilterRegistrationBean<SetSystemNameFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(filter);
        registrationBean.setUrlPatterns(new ArrayList<>(Arrays.asList("/*")));
        return registrationBean;
    }
}
