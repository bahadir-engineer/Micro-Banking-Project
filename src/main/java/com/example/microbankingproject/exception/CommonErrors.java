package com.example.microbankingproject.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public enum CommonErrors {
    ACCOUNT_NOT_FOUND("ACCOUNT_NOT_FOUND", HttpStatus.NOT_FOUND,"Hesab mövcud deyil", LocalDateTime.now()),
    PHONE_ALREADY_REGISTERED("PHONE_ALREADY_REGISTERED", HttpStatus.FOUND, "Bu telefon nömrəsi ilə artıq qeydiyyatdan keçilib", LocalDateTime.now()),
    OTP_CODE_EXPIRED("OTP_CODE_EXPIRED", HttpStatus.UNAUTHORIZED, "OTP kodun etibarlılıq müddəti bitib", LocalDateTime.now()),
    OTP_CODE_INCORRECT("OTP_CODE_INCORRECT", HttpStatus.UNAUTHORIZED, "OTP kod düzgün deyil", LocalDateTime.now()),
    INVALID_TOKENS("INVALID_TOKENS", HttpStatus.UNAUTHORIZED, "Tokenlər etibarlı deyil", LocalDateTime.now()),
    CARD_NOT_FOUND("CARD_NOT_FOUND", HttpStatus.NOT_FOUND, "Kart mövcud deyil", LocalDateTime.now()),
    CARD_BLOCKED("CARD_BLOCKED", HttpStatus.FORBIDDEN, "Kart bloklanıb", LocalDateTime.now()),
    INSUFFICIENT_BALANCE("INSUFFICIENT_BALANCE", HttpStatus.BAD_REQUEST, "Kartda kifayət qədər balans yoxdur", LocalDateTime.now());

    private final String key;
    private final HttpStatus status;
    private final String message;
    private final LocalDateTime timestamp;
}
