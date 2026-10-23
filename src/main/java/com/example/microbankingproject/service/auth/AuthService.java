package com.example.microbankingproject.service.auth;

import com.example.microbankingproject.dto.auth.AuthenticationDto;
import com.example.microbankingproject.dto.request.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.core.userdetails.UserDetails;

public interface AuthService {

    void signUp(UserRegistrationDto userRegistrationDto);

    void loginPhone(LoginPhoneDto loginPhoneDto);

    AuthenticationDto verifyOTP(OTPRequestDto otpRequestDto, HttpServletResponse response);

    void resendOTP(ResendOTPRequestDto resendOTPRequestDto);

//    AuthenticationDto refreshToken(TokenRequestDto tokenRequestDto, HttpServletRequest request, HttpServletResponse response);

    void logout(TokenRequestDto tokenRequestDto, HttpServletRequest request, HttpServletResponse response);
}
