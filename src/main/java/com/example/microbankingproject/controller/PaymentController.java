package com.example.microbankingproject.controller;

import com.example.microbankingproject.dto.request.PaymentRequestDto;
import com.example.microbankingproject.dto.response.PaymentResponseDto;
import com.example.microbankingproject.service.PaymentService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/v1/payment")
public class PaymentController {

    PaymentService paymentService;

    @PostMapping("/unsafe")
    public PaymentResponseDto payUnsafe(@Valid @RequestBody PaymentRequestDto paymentRequestDto) {
        return paymentService.payUnsafe(paymentRequestDto);
    }

    @PostMapping("/safe")
    public PaymentResponseDto paySafe(@Valid @RequestBody PaymentRequestDto paymentRequestDto) {
        return paymentService.paySafe(paymentRequestDto);
    }
}
