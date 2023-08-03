/*
 * Developed by Wiryaimd
 * Copyright (c) 2023 Shinigami ID
 * All rights reserved.
 */

package com.shinigami.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TransactionDetail{

        @SerializedName("order_id")
        private String orderId;

        @SerializedName("gross_amount")
        private String price;

    }

    @SerializedName("transaction_details")
    private TransactionDetail transactionDetail;


}
