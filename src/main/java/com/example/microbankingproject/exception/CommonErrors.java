package com.example.microbankingproject.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public enum CommonErrors {
    INVALID_PRICE_RANGE("INVALID_PRICE_RANGE", HttpStatus.BAD_REQUEST,"Minimum qiymət maksimum qiymətdən böyük ola bilməz", LocalDateTime.now()),
    ACCOUNT_NOT_FOUND("ACCOUNT_NOT_FOUND", HttpStatus.NOT_FOUND,"Hesab mövcud deyil", LocalDateTime.now()),
    INVALID_CONTENT_TYPE("INVALID_CONTENT_TYPE", HttpStatus.BAD_REQUEST, "Daxil edilən fayl şəkil və ya video deyil", LocalDateTime.now()),
    PHONE_ALREADY_REGISTERED("PHONE_ALREADY_REGISTERED", HttpStatus.FOUND, "Bu telefon nömrəsi ilə artıq qeydiyyatdan keçilib", LocalDateTime.now()),
    OTP_CODE_EXPIRED("OTP_CODE_EXPIRED", HttpStatus.UNAUTHORIZED, "OTP kodun etibarlılıq müddəti bitib", LocalDateTime.now()),
    OTP_CODE_INCORRECT("OTP_CODE_INCORRECT", HttpStatus.UNAUTHORIZED, "OTP kod düzgün deyil", LocalDateTime.now()),
    INVALID_TOKENS("INVALID_TOKENS", HttpStatus.UNAUTHORIZED, "Tokenlər etibarlı deyil", LocalDateTime.now()),
    USER_IMAGE_NOT_FOUND("USER_IMAGE_NOT_FOUND", HttpStatus.NOT_FOUND, "Profil şəkli mövcud deyil", LocalDateTime.now());

    private final String key;
    private final HttpStatus status;
    private final String message;
    private final LocalDateTime timestamp;
}
