//package com.example.microbankingproject.config;
//
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class RateLimitingConfig {
//    @Bean
//    public FilterRegistrationBean<RateLimitingFilter> rateLimitFilter() {
//        FilterRegistrationBean<RateLimitingFilter> registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(new RateLimitingFilter());
//        registrationBean.addUrlPatterns("/api/auth/**");
//        return registrationBean;
//    }
//}