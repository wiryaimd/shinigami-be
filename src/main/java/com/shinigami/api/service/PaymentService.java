/*
 * Developed by Wiryaimd
 * Copyright (c) 2023 Shinigami ID
 * All rights reserved.
 */

package com.shinigami.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.shinigami.api.dto.PaymentDto;
import com.shinigami.api.dto.PaymentValidateDto;
import com.shinigami.api.exception.PaymentCancelException;
import com.shinigami.api.exception.PaymentNotCompleteException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

//    private final AppProperties appProperties;
    private final UserService userService;

    @Value("${midtrans.secretkey}")
    private String midtransSecret;

    private Gson gson = new Gson();

    public String process(PaymentDto paymentDto) {

        String auth = Base64.getEncoder().encodeToString(midtransSecret.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Basic " + auth);

        HttpEntity<String> request = new HttpEntity<>(gson.toJson(paymentDto, PaymentDto.class), headers);

        String response = new RestTemplate().postForObject(
                "https://app.sandbox.midtrans.com/snap/v1/transactions",
//                "https://app.midtrans.com/snap/v1/transactions",
                request,
                String.class
        );

        log.info("res borr: {}" + response);

        return response;
    }


    public void validate(PaymentValidateDto paymentValidateDto) {
        String url = String.format("https://app.midtrans.com/snap/v1/transactions/%s/status", paymentValidateDto.getToken());
        ResponseEntity<String> response = new RestTemplate().getForEntity(url, String.class);

        String body = response.getBody();
        String paymentTime;

        log.info("res body: " + body);

        int code;
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(body);
//            code = Integer.parseInt(new JSONObject(body).getString("status_code"));
            code = jsonNode.get("status_code").asInt();
        } catch (Exception e) {
            throw new PaymentCancelException("payment cancel");
        }

        if (code != 200){
            throw new PaymentNotCompleteException("payment not complete", code);
        }

        try {
            JsonNode jsonNode = new ObjectMapper().readTree(body);
            paymentTime = jsonNode.get("settlement_time").asText();

            log.info("settlement time: {}", paymentTime);
        } catch (Exception e) {
            throw new PaymentNotCompleteException("payment not complete 2", code);
        }

        LocalDateTime time = LocalDateTime.parse(paymentTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        userService.setPremium(paymentValidateDto.getEmail(), paymentValidateDto.getDay(), time);
    }
}
