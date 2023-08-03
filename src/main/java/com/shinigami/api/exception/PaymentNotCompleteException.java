/*
 * Developed by Wiryaimd
 * Copyright (c) 2023 Shinigami ID
 * All rights reserved.
 */

package com.shinigami.api.exception;

public class PaymentNotCompleteException extends RuntimeException{

    private int code;

    public PaymentNotCompleteException(String message, int code) {
        super(message);

        this.code = code;
    }

    public PaymentNotCompleteException(String message, Throwable cause) {
        super(message, cause);
    }

    public int getCode() {
        return code;
    }
}
