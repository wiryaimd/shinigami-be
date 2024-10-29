/*
 * Developed by Wiryaimd
 * Copyright (c) 2023 Shinigami ID
 * All rights reserved.
 */

package com.shinigami.api.config;

import com.google.gson.Gson;
import com.shinigami.api.filter.ChapterFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public FilterRegistrationBean<ChapterFilter> chapterFilter(){
        FilterRegistrationBean<ChapterFilter> filter = new FilterRegistrationBean<>();
        filter.setFilter(new ChapterFilter());
        filter.addUrlPatterns("/api/v1/comic/*", "/api/v1/filter/*", "/api/v1/projects", "/api/v1/search/*", "/api/v1/mirror", "/api/v1/chapter/*", "/api/v1/projects", "/api/v1/browse", "/api/v2/*");
        return filter;
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    public Gson gson(){
        return new Gson();
    }

}
