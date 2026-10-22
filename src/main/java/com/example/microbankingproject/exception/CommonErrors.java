package com.example.microbankingproject.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public enum CommonErrors {
    INVALID_PRICE_RANGE("INVALID_PRICE_RANGE", HttpStatus.BAD_REQUEST,"Minimum qiymət maksimum qiymətdən böyük ola bilməz", LocalDateTime.now()),
    INVALID_FLOOR_RANGE("INVALID_FLOOR_RANGE", HttpStatus.BAD_REQUEST,"Minimum mərtəbə sayı maksimum mərtəbə sayından böyük ola bilməz", LocalDateTime.now()),
    INVALID_AREA_RANGE("INVALID_AREA_RANGE", HttpStatus.BAD_REQUEST,"Minimum sahə maksimum sahədən böyük ola bilməz", LocalDateTime.now()),
    BANNER_NOT_FOUND("BANNER_NOT_FOUND", HttpStatus.NOT_FOUND, "Banner mövcud deyil", LocalDateTime.now()),
    BANNER_IMAGE_NOT_FOUND("BANNER_IMAGE_NOT_FOUND", HttpStatus.NOT_FOUND, "Banner şəkli mövcud deyil", LocalDateTime.now()),
    BLOG_NOT_FOUND("BLOG_NOT_FOUND", HttpStatus.NOT_FOUND, "Blog mövcud deyil", LocalDateTime.now()),
    BLOG_IMAGE_NOT_FOUND("BLOG_IMAGE_NOT_FOUND", HttpStatus.NOT_FOUND, "Blog şəkli mövcud deyil", LocalDateTime.now()),
    FAVORITE_NOT_FOUND("FAVORITE_NOT_FOUND", HttpStatus.NOT_FOUND, "Seçilmiş elan mövcud deyil", LocalDateTime.now()),
    FAVORITES_NOT_FOUND("FAVORITES_NOT_FOUND", HttpStatus.NOT_FOUND, "Seçilmiş elanlar yoxdur", LocalDateTime.now()),
    ANNOUNCEMENT_NOT_FOUND("ANNOUNCEMENT_NOT_FOUND", HttpStatus.NOT_FOUND, "Elan mövcud deyil", LocalDateTime.now()),
    NEIGHBORHOOD_NOT_FOUND("NEIGHBORHOOD_NOT_FOUND", HttpStatus.NOT_FOUND, "Qonşuluq əmlakı mövcud deyil", LocalDateTime.now()),
    NEIGHBORHOOD_IMAGE_NOT_FOUND("NEIGHBORHOOD_IMAGE_NOT_FOUND", HttpStatus.NOT_FOUND, "Qonşuluq əmlakı şəkli mövcud deyil", LocalDateTime.now()),
    ACCOUNT_NOT_FOUND("ACCOUNT_NOT_FOUND", HttpStatus.NOT_FOUND,"Hesab mövcud deyil", LocalDateTime.now()),
    INVALID_CONTENT_TYPE("INVALID_CONTENT_TYPE", HttpStatus.BAD_REQUEST, "Daxil edilən fayl şəkil və ya video deyil", LocalDateTime.now()),
    MEDIA_NOT_FOUND("MEDIA_NOT_FOUND", HttpStatus.NOT_FOUND, "Media mövcud deyil", LocalDateTime.now()),
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
