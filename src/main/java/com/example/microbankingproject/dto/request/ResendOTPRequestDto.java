package com.example.microbankingproject.dto.request;

import com.example.investhome.entity.enums.EntranceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ResendOTPRequestDto {
    @NotNull(message = "Giriş tipi mütləq qeyd edilməlidir")
    private EntranceType entranceType;
    @Pattern(regexp = "^\\+[1-9]\\d{7,14}$", message = "Telefon nömrəsini düzgün daxil edin")
    @NotBlank(message = "Telefon nömrəsi hissəsi doldurulmalıdır")
    private String phone;
}
