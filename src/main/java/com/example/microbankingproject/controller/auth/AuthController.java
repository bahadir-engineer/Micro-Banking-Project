package com.example.microbankingproject.controller.auth;

import com.example.microbankingproject.dto.auth.AuthenticationDto;
import com.example.microbankingproject.dto.request.*;
import com.example.microbankingproject.service.auth.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/v1/auth")
public class AuthController {

    AuthService authService;

    @PostMapping("/sign/up")
    public void signUp(@Valid @RequestBody UserRegistrationDto userRegistrationDto) {
        authService.signUp(userRegistrationDto);
    }

    @PostMapping("/login/phone")
    public void loginPhone(@Valid @RequestBody LoginPhoneDto loginPhoneDto) {
        authService.loginPhone(loginPhoneDto);
    }

    @PostMapping("/verify/OTP")
    public AuthenticationDto verifyOTP(@Valid @RequestBody OTPRequestDto otpRequestDto,
                                       HttpServletResponse response) {
        return authService.verifyOTP(otpRequestDto, response);
    }

    @PostMapping("/resend/OTP")
    public void resendOTP(@Valid @RequestBody ResendOTPRequestDto resendOTPRequestDto) {
        authService.resendOTP(resendOTPRequestDto);
    }

//    @PostMapping("/refresh/token")
//    public AuthenticationDto refreshToken(@Valid @RequestBody TokenRequestDto tokenRequestDto,
//                                          HttpServletRequest request,
//                                          HttpServletResponse response) {
//        return authService.refreshToken(tokenRequestDto, request, response);
//    }

    @PostMapping("/logout")
    public void logout(@Valid @RequestBody TokenRequestDto tokenRequestDto,
                       HttpServletRequest request,
                       HttpServletResponse response) {
        authService.logout(tokenRequestDto, request, response);
    }
}