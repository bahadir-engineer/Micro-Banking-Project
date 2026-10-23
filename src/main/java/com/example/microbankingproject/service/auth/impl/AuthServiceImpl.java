package com.example.microbankingproject.service.auth.impl;

import com.example.microbankingproject.dto.auth.AuthenticationDto;
import com.example.microbankingproject.dto.request.*;
import com.example.microbankingproject.entity.UserEntity;
import com.example.microbankingproject.entity.enums.EntranceType;
import com.example.microbankingproject.entity.enums.Roles;
import com.example.microbankingproject.entity.enums.TokenType;
import com.example.microbankingproject.exception.*;
import com.example.microbankingproject.mapper.UserMapper;
import com.example.microbankingproject.repository.UserRepository;
import com.example.microbankingproject.service.auth.AuthService;
//import com.example.microbankingproject.service.auth.AuthUserDetailsService;
import com.example.microbankingproject.service.auth.JwtBlacklistService;
import com.example.microbankingproject.service.auth.JwtService;
import com.example.microbankingproject.utils.OTPCodeGenerator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class AuthServiceImpl implements AuthService {

    UserRepository userRepository;
    UserMapper userMapper;
    JwtService jwtService;
    JwtBlacklistService jwtBlacklistService;
//    AuthUserDetailsService authUserDetailsService;
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public void signUp(UserRegistrationDto userRegistrationDto) {
        log.info("Sign Up Started...");

        String phone = userRegistrationDto.getPhone();
        String keyOTP = phone + ":OTP";
        String keyData = phone + ":Data";

        if (phoneExists(phone)) {
            throw new ApplicationException(CommonErrors.PHONE_ALREADY_REGISTERED);
        }

        String OTP = OTPCodeGenerator.generateCode();
        redisTemplate.opsForValue().set(keyOTP, OTP, 60, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(keyData, userRegistrationDto, 600, TimeUnit.SECONDS);
//        smsService.sendOTP(phone, OTP);

        log.info("Sign Up Ended");
    }

    @Override
    public void loginPhone(LoginPhoneDto loginPhoneDto) {
        log.info("Login Phone Started...");

        String phone = loginPhoneDto.getPhone();
        UserDetails account = getUserByPhone(phone);
        String keyOTP = phone + ":OTP";

        if (account == null) {
            throw new ApplicationException(CommonErrors.ACCOUNT_NOT_FOUND);
        }

        String OTP = OTPCodeGenerator.generateCode();
        redisTemplate.opsForValue().set(keyOTP, OTP, 60, TimeUnit.SECONDS);
//        smsService.sendOTP(phone, OTP);

        log.info("Login Phone Ended");
    }

    @Override
    @Transactional
    public AuthenticationDto verifyOTP(OTPRequestDto otpRequestDto, HttpServletResponse response) {
        log.info("Verify OTP Started...");

        String phone = otpRequestDto.getPhone();
        String keyOTP = phone + ":OTP";
        String keyData = phone + ":Data";

        String inputOTP = otpRequestDto.getOTP();
        String cachedOTP = (String) redisTemplate.opsForValue().get(keyOTP);
        Long TTL = redisTemplate.getExpire(keyOTP, TimeUnit.SECONDS);

        if (expiryOTP(TTL)) {
            throw new ApplicationException(CommonErrors.OTP_CODE_EXPIRED);
        }

        if (checkOTP(cachedOTP, inputOTP)) {
            throw new ApplicationException(CommonErrors.OTP_CODE_INCORRECT);
        }

        UserDetails account;
        if (EntranceType.SIGNUP.equals(otpRequestDto.getEntranceType())) {
            UserRegistrationDto userRegistrationDto = (UserRegistrationDto) redisTemplate.
                    opsForValue().get(keyData);
            account = (UserDetails) userMapper.
                    mapUserRegistrationDtoToEntity(userRegistrationDto);
            setRolesAndSave((UserEntity) account);

            redisTemplate.delete(keyOTP);
            redisTemplate.delete(keyData);
        } else {
            account = getUserByPhone(phone);
            redisTemplate.delete(keyOTP);
        }

        String refreshToken = generateRefreshToken(account);
        String accessToken = generateAccessToken(account);
        return AuthenticationDto.
                builder().
                accessToken(accessToken).
                refreshToken(refreshToken).
                build();
    }

    @Override
    public void resendOTP(ResendOTPRequestDto resendOTPRequestDto) {
        log.info("Resend OTP Started...");

        String phone = resendOTPRequestDto.getPhone();
        String keyOTP = phone + ":OTP";
        String keyData = phone + ":Data";
        String OTP = OTPCodeGenerator.generateCode();

        if (EntranceType.SIGNUP.equals(resendOTPRequestDto.getEntranceType())) {
            UserRegistrationDto userRegistrationDto = (UserRegistrationDto) redisTemplate.opsForValue().get(keyData);
            redisTemplate.opsForValue().set(keyData, userRegistrationDto, 600, TimeUnit.SECONDS);
        }

        redisTemplate.opsForValue().set(keyOTP, OTP, 60, TimeUnit.SECONDS);
//        smsService.sendOTP(phone, OTP);

        log.info("Resend OTP Ended");
    }

//    @Override
//    public AuthenticationDto refreshToken(AccessTokenRequestDto accessTokenRequestDto,
//                                          HttpServletRequest request,
//                                          HttpServletResponse response) {
//        log.info("Refresh Token Started...");
//
//        String accessToken = accessTokenRequestDto.getAccessToken();
//        jwtBlacklistService.addBlacklist(accessToken, 19L);
//
//        String refreshToken = getRefreshToken(request);
//        try {
//            String username = jwtService.extractUsername(refreshToken);
//            UserDetails userDetails = authUserDetailsService.loadUserByUsername(username);
//            if (jwtService.isTokenValid(refreshToken, userDetails) &&
//                    !jwtBlacklistService.isBlacklisted(refreshToken)) {
//                return generateAccessToken(userDetails);
//            }
//        } catch (ExpiredJwtException exception) {
//            log.info("JWT Expired: " + exception.getMessage());
//        }
//
//        jwtBlacklistService.addBlacklist(refreshToken, 43200L);
//
//        throw new ApplicationException(CommonErrors.INVALID_TOKENS);
//    }

    @Override
    public void logout(TokenRequestDto tokenRequestDto,
                       HttpServletRequest request,
                       HttpServletResponse response) {
        log.info("Log Out Started...");

        String accessToken = tokenRequestDto.getAccessToken();
        String refreshToken = tokenRequestDto.getRefreshToken();
        jwtBlacklistService.addBlacklist(accessToken, 19L);
        jwtBlacklistService.addBlacklist(refreshToken, 43200L);

        log.info("Log Out Ended");
    }

    public UserDetails getAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserDetails) authentication.getPrincipal();
    }

    private boolean phoneExists(String phone) {
        return userRepository.findByPhone(phone) != null;
    }

    private boolean checkOTP(String cachedOTP, String OTP) {
        return cachedOTP != null && !cachedOTP.equals(OTP);
    }

    private boolean expiryOTP(Long TTL) {
        return TTL == null || TTL < 0;
    }

    private void setRolesAndSave(UserEntity userEntity) {
        Set<Roles> roles = new HashSet<>();
        roles.add(Roles.USER);
        userEntity.setRoles(roles);
        userRepository.save(userEntity);
    }

    private UserDetails getUserByPhone(String phone) {
        UserDetails account = (UserDetails) userRepository.findByPhoneWithRoles(phone);
        return account;
    }

    private String generateAccessToken(UserDetails user) {
        return jwtService.generateToken(user, TokenType.ACCESS);
    }

    private String generateRefreshToken(UserDetails user) {
        return jwtService.generateToken(user, TokenType.REFRESH);
    }
}
