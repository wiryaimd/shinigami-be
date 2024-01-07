/*
 * Developed by Wiryaimd
 * Copyright (c) 2023 Shinigami ID
 * All rights reserved.
 */

package com.shinigami.api.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class ChapterFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String key = request.getHeader("lulthings");
        log.info("info hooh " + key);

        if (key == null){
            response.setStatus(200);
            return;
        }

        if (!key.equalsIgnoreCase("iyainiyainiyainde123")){
            response.setStatus(200);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
