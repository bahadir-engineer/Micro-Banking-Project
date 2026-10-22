package com.example.microbankingproject.service.auth;

import com.example.microbankingproject.dto.auth.AuthenticationDto;
import com.example.microbankingproject.dto.request.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthService {

    void signUp(UserRegistrationDto userRegistrationDto);

    void loginPhone(LoginPhoneDto loginPhoneDto);

    AuthenticationDto loginWithGoogle(AuthCodeRequestDto authCodeRequestDto, HttpServletResponse response);

    AuthenticationDto loginWithApple(AuthCodeRequestDto authCodeRequestDto, HttpServletResponse response) throws Exception;

    AuthenticationDto verifyOTP(OTPRequestDto otpRequestDto, HttpServletResponse response);

    void resendOTP(ResendOTPRequestDto resendOTPRequestDto);

    AuthenticationDto refreshToken(AccessTokenRequestDto accessTokenRequestDto, HttpServletRequest request, HttpServletResponse response);

    void logout(AccessTokenRequestDto accessTokenRequestDto, HttpServletRequest request, HttpServletResponse response);

    String callbackGoogle(String authCode);

    String callbackApple(String authCode);

    UserDetails getAccount();
}
