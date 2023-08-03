/*
 * Developed by Wiryaimd
 * Copyright (c) 2023 Shinigami ID
 * All rights reserved.
 */

package com.shinigami.api.exception;

import org.springframework.boot.configurationprocessor.json.JSONException;

public class PaymentCancelException extends RuntimeException {

    public PaymentCancelException(String message) {
        super(message);
    }

    public PaymentCancelException(String message, Throwable cause) {
        super(message, cause);
    }
}
