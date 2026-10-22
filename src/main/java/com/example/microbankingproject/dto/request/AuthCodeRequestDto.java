package com.example.microbankingproject.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AuthCodeRequestDto {
    @NotBlank(message = "Authorization Code boş olmamalıdır")
    private String authCode;
}
