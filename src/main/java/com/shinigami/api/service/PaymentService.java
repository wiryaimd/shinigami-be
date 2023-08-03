/*
 * Developed by Wiryaimd
 * Copyright (c) 2023 Shinigami ID
 * All rights reserved.
 */

package com.shinigami.api.service;

import com.shinigami.api.config.AppProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

//    private final AppProperties appProperties;

    @Value("${midtrans.secretkey}")
    private String midtransSecret;

    public String process(String json) {

        String auth = Base64.getEncoder().encodeToString(midtransSecret.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Basic " + auth);

        HttpEntity<String> request = new HttpEntity<>(json, headers);

        String response = new RestTemplate().postForObject(
                "https://app.sandbox.midtrans.com/snap/v1/transactions",
                request,
                String.class
        );

        log.info("res borr: {}" + response);

        return response;
    }



}
