/*
 * Developed by Wiryaimd
 * Copyright (c) 2023 Shinigami ID
 * All rights reserved.
 */

package com.shinigami.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/midtrans")
@Slf4j
public class PaymentController {

    @PostMapping("/charge")
    public ResponseEntity<String> midtransCharge(@RequestBody String json){
        log.info("midtrans charge post: {}", json);
        return ResponseEntity.ok(json);
    }

}
