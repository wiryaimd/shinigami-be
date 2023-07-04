/*
 * Developed by Wiryaimd
 * Copyright (c) 2023 Shinigami ID
 * All rights reserved.
 */

package com.shinigami.api.exception;

public class ScrapFailException extends Exception{

    public ScrapFailException(String message) {
        super(message);
    }

    public ScrapFailException(String message, Throwable cause) {
        super(message, cause);
    }
}
