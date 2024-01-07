/*
 * Developed by Wiryaimd
 * Copyright (c) 2024 Shinigami ID
 * All rights reserved.
 */

package com.shinigami.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EncDto {

    private String ct, iv, s;

}
