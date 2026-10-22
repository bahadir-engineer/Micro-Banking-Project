package com.example.microbankingproject.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserRegistrationDto {
    @Pattern(regexp = "^[A-ZƏÖÜĞÇŞİa-zəöüğçşi]+\\s+[A-ZƏÖÜĞÇŞİa-zəöüğçşi]+$", message = "Ad və Soyad hər ikisi daxil edilməlidir")
    @NotBlank(message = "Ad və Soyad hissəsi doldurulmalıdır")
    private String fullName;
    @Pattern(regexp = "^\\+[1-9]\\d{7,14}$", message = "Telefon nömrəsini düzgün daxil edin")
    @NotBlank(message = "Telefon nömrəsi hissəsi doldurulmalıdır")
    private String phone;
}