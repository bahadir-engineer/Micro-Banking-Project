package com.example.microbankingproject.dto.response;

import com.example.microbankingproject.entity.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PaymentResponseDto {
    private UUID transactionNumber;
    private TransactionStatus transactionStatus;
    private Integer cardNumber;
    private Long balanceAfter;
}
