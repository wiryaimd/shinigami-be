/*
 * Developed by Wiryaimd
 * Copyright (c) 2023 Shinigami ID
 * All rights reserved.
 */

package com.shinigami.api.controller;

import com.shinigami.api.dto.PaymentDto;
import com.shinigami.api.dto.PaymentValidateDto;
import com.shinigami.api.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/midtrans")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/charge")
    public ResponseEntity<String> midtransCharge(@RequestBody PaymentDto paymentDto){
        return ResponseEntity.ok(paymentService.process(paymentDto));
    }

    @PostMapping("/validate")
    public ResponseEntity<Void> midtransValidate(@RequestBody PaymentValidateDto paymentValidateDto, @RequestParam("auth") String auth){
        if (!auth.equals("its just like seeing her for the first time")){
            return ResponseEntity.status(404).build();
        }

        paymentService.validate(paymentValidateDto);

        return ResponseEntity.ok(null);
    }

}
