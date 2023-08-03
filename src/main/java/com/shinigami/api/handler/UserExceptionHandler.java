/*
 * Developed by Wiryaimd
 * Copyright (c) 2023 Shinigami ID
 * All rights reserved.
 */

package com.shinigami.api.handler;

import com.shinigami.api.exception.ElementNotFoundException;
import com.shinigami.api.exception.PaymentCancelException;
import com.shinigami.api.exception.PaymentNotCompleteException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(ElementNotFoundException.class)
    public ResponseEntity<String> err1(Exception e){
        return ResponseEntity.status(404).body(e.getMessage());
    }

    @ExceptionHandler(PaymentNotCompleteException.class)
    public ResponseEntity<String> paymentNotComplete(PaymentNotCompleteException e){
        return ResponseEntity.status(e.getCode()).body(String.format("""
                { "msg": "%s" }
                """, e.getMessage()));
    }

    @ExceptionHandler(PaymentCancelException.class)
    public ResponseEntity<String> paymentCancel(Exception e){
        return ResponseEntity.status(400).body(String.format("""
                { "msg": "%s" }
                """, e.getMessage()));
    }

}
