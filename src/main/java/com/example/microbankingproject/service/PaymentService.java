package com.example.microbankingproject.service;

import com.example.microbankingproject.dto.request.PaymentRequestDto;
import com.example.microbankingproject.dto.response.PaymentResponseDto;

public interface PaymentService {
    PaymentResponseDto payUnsafe(PaymentRequestDto paymentRequestDto);
    PaymentResponseDto paySafe(PaymentRequestDto paymentRequestDto);
}
