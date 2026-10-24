package com.example.microbankingproject.dto.request;

import com.example.microbankingproject.validation.SixteenDigitCardNumber;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PaymentRequestDto {
    @NotNull(message = "Kart nömrəsi mütləq qeyd edilməlidir")
    @SixteenDigitCardNumber
    private Long cardNumber;
    @NotNull(message = "Məbləğ mütləq qeyd edilməlidir")
    @Positive(message = "Məbləğ sıfırdan böyük olmalıdır")
    private Long amount;
}
